package com.naresh.youtubeclone.controller;

import com.naresh.youtubeclone.dto.CommentDto;
import com.naresh.youtubeclone.dto.VideoDto;
import com.naresh.youtubeclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void  uploadVideo(@RequestParam("file") MultipartFile file) {
         videoService.uploadVideo(file);
    }

    //api/endpt to get uploaad the thumbnail
    //so whenever the user upload the thumbnail file along with the videoId we will upload to the s3 and retrieve the thumbnail url and return to the client as res.
    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file") MultipartFile file, @RequestParam("videoId") String videoId) {
        return videoService.uploadThumbnail(file, videoId);
    }


//    lets impl the api to get the video metadata
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    //will receive the metadata from the req body from the client
    public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    //api/endpt to get the video details, so we can play in our player
    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/disLike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto disLikeVideo(@PathVariable String videoId) {
        return videoService.disLikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos() {
        return videoService.getAllVideos();
    }

}
