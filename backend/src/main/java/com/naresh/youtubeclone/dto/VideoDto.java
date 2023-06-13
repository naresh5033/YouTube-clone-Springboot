package com.naresh.youtubeclone.dto;

import com.naresh.youtubeclone.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private Integer likeCount; // here we re using the int instead of atomic integer because, with the dto we re not making any calculations, just using this as data transfer only
    private Integer dislikeCount;
    private Integer viewCount;
}
