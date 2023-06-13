package com.naresh.youtubeclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//since we already impl the video upload() but it doesn't return anything, so we need to get the video id from the db
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoResponse {
    private String videoId; //will be generated after saving to the database
    private String videoUrl; //automatically generated whenever the video is uploaded to s3
}
