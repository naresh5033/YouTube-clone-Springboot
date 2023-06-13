import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UploadVideoResponse} from "./upload-video/UploadVideoResponse";
import {VideoDto} from "./video-dto";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient: HttpClient) {
  }

// lets make a http post request to the server (to upload a video)
// since its observable and it will look for the seq of stream events
  uploadVideo(fileEntry: File): Observable<UploadVideoResponse> {
    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name);

    // HTTP Post call to upload the video
    return this.httpClient.post<UploadVideoResponse>("http://localhost:8080/api/videos", formData);
  }

  uploadThumbnail(fileEntry: File, videoId: string): Observable<string> {
    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name);
    
    // and we need some additional params 
    formData.append('videoId', videoId);

    // HTTP Post call to upload the thumbnail
    return this.httpClient.post("http://localhost:8080/api/videos/thumbnail", formData, {
      responseType: 'text'
    });
  }

  //http call to our backend to get the video.
  getVideo(videoId: string): Observable<VideoDto> {
    return this.httpClient.get<VideoDto>("http://localhost:8080/api/videos/" + videoId);
  }

  //http (put)call to our backend to save the video along with the metadata.
  saveVideo(videoMetaData: VideoDto): Observable<VideoDto> {
    return this.httpClient.put<VideoDto>("http://localhost:8080/api/videos", videoMetaData);
  }

  //get all the videos
  getAllVideos(): Observable<Array<VideoDto>> {
    return this.httpClient.get<Array<VideoDto>>("http://localhost:8080/api/videos");
  }

  //http call to the liked videos
  likeVideo(videoId: string): Observable<VideoDto> {
    return this.httpClient.post<VideoDto>("http://localhost:8080/api/videos/" + videoId + "/like", null); // for this post call we don't ve any body, so pass it null for the body
  }

  disLikeVideo(videoId: string): Observable<VideoDto> {
    return this.httpClient.post<VideoDto>("http://localhost:8080/api/videos/" + videoId + "/disLike", null);
  }
}
