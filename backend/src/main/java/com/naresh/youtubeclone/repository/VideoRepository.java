package com.naresh.youtubeclone.repository;

import com.naresh.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> { // the prim key is str
}
