package com.naresh.youtubeclone.service;

import com.naresh.youtubeclone.repository.VideoRepository;
import com.naresh.youtubeclone.dto.CommentDto;
import com.naresh.youtubeclone.dto.UploadVideoResponse;
import com.naresh.youtubeclone.dto.VideoDto;
import com.naresh.youtubeclone.model.Comment;
import com.naresh.youtubeclone.model.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    //lets use our S3 upload service here to upload our file
    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserService userService;

    //upload the video and get the id and url of the video
    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);
        //and save the video obj to the mongo
        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());

    } //now we can call this upload service in our controller

//    to upload the video metadata into the database
    public VideoDto editVideo(VideoDto videoDto) {
        // Find the video by videoId
        var savedVideo = getVideoById(videoDto.getId());
        // Map the videoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        // save the video  to the database
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        var savedVideo = getVideoById(videoId);

        //upload the file, save the thumbnail url and save to the database
        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by id - " + videoId));
    }

    //so we can play in the videogular player
    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);

        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId); //once increments the like count then save to the video history

        return mapToVideoDto(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save(savedVideo);
    }

    public VideoDto likeVideo(String videoId) {
        Video videoById = getVideoById(videoId);
        //we ve to add the like count and if the user already liked then dislike the video, and vice versa
        if (userService.ifLikedVideo(videoId)) { // if the user already liked
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifDisLikedVideo(videoId)) { // if the user already disliked
            videoById.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else { //else add to liked videos
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    public VideoDto disLikeVideo(String videoId) {
        Video videoById = getVideoById(videoId);

        if (userService.ifDisLikedVideo(videoId)) { //if user already disliked
            videoById.decrementDisLikes();
            userService.removeFromDislikedVideos(videoId);
        } else if (userService.ifLikedVideo(videoId)) { //already liked
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else { //add to   the disliked videos
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }
        // once we liked the video save it to the database
        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    //map the video object to the videoDto
    private VideoDto mapToVideoDto(Video videoById) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDisLikes().get());
        videoDto.setViewCount(videoById.getViewCount().get());
        return videoDto;
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);

        videoRepository.save(video);
    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();

        //return commentList and (map them to the commentDto)
        return commentList.stream().map(this::mapToCommentDto).toList(); //we can also use lambda instead of method reference .map(comment -> mapToCommentDto(comment))
    }

    //map the comment to the commentDto
    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream().map(this::mapToVideoDto).toList();
    }
}
