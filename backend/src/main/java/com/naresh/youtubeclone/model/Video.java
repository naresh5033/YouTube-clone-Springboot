package com.naresh.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value = "Video")
@Data // the data annotation from lombok to reduce the getters and setter, and no, all args contr
//in the structure tab we can see the lombok  has created all the setters getters, eq, tostring etc (for all our fields) at the compilation
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    private String id; //the id in the str form not in the obj form (as the db will take the id as a obj form)
    private String title;
    private String description;
    private String userId;
    private AtomicInteger likes = new AtomicInteger(0); // if we use only Integer then in a multithreaded env it will not happen synchronously, so we can use AtomicInteger(atomically increment the counters), this way our likes will sync in the multithreaded env.
    private AtomicInteger disLikes = new AtomicInteger(0);
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus; // from the enum
    private AtomicInteger viewCount = new AtomicInteger(0);
    private String thumbnailUrl;
    private List<Comment> commentList = new CopyOnWriteArrayList<>(); // type comment class

    public void incrementLikes() {
        likes.incrementAndGet();
    }

    public void decrementLikes() {
        likes.decrementAndGet();
    }

    public void incrementDisLikes() {
        disLikes.incrementAndGet();
    }

    public void decrementDisLikes() {
        disLikes.decrementAndGet();
    }

    public void incrementViewCount() {
        viewCount.incrementAndGet();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}
