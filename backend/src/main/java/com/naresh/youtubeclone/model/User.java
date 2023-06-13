package com.naresh.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String emailAddress;
    private String sub;
    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet(); //the list will maintains the user subscribed to the other users
    private Set<String> subscribers = ConcurrentHashMap.newKeySet(); //so this way our set will be thread safe, and also we wont get any null point exception
    private Set<String> videoHistory = ConcurrentHashMap.newKeySet();
    private Set<String> likedVideos = ConcurrentHashMap.newKeySet();
    private Set<String> disLikedVideos = ConcurrentHashMap.newKeySet();

    public void addToLikeVideos(String videoId) {
        likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId) {
        likedVideos.remove(videoId);
    }

    public void removeFromDislikedVideos(String videoId) {
        disLikedVideos.remove(videoId);
    }

    public void addToDislikedVideos(String videoId) {
        disLikedVideos.add(videoId);
    }

    public void addToVideoHistory(String videoId) {
        videoHistory.add(videoId);
    }

    public void addToSubscribedToUsers(String userId) {
        subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String userId) {
        subscribers.add(userId);
    }

    public void removeFromSubscribedToUsers(String userId) {
        subscribedToUsers.remove(userId);
    }

    public void removeFromSubscribers(String userId) {
        subscribers.remove(userId);
    }
}
