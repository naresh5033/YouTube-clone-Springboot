package com.naresh.youtubeclone.repository;

import com.naresh.youtubeclone.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


//save our user details, as part of the user registration we need some user's info.. from the oAuth discovery Document we can ggt the endpt for the user info
//so we can call this user info endpoint(https://dev-w2xkb2uincv1wiyp.us.auth0.com/userinfo)(from our Spring Boot app) to get the user info..
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findBySub(String sub);
}
