package com.naresh.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naresh.youtubeclone.dto.UserInfoDTO;
import com.naresh.youtubeclone.model.User;
import com.naresh.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue) { //ve to make a call to the userInfo endpoint and then fetch the user details and save it to the DB.
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue)) //bearer space token value
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        //now we ve the http req and the client, lets send the request via http client
        try {
            HttpResponse<String> responseString = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = responseString.body();

            //we ve the res string now ve to convert into a java object(User Info DTO)
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //since we re not reading all the props of the user(so the obj mapper will fail), we just specified few fields in the user info dto, so we ve to set it as false
            UserInfoDTO userInfoDTO = objectMapper.readValue(body, UserInfoDTO.class); // read the body and parse it to userinfo dto

            //check to see if the user is already in the DB
            Optional<User> userBySubject = userRepository.findBySub(userInfoDTO.getSub()); //find by subject
            if(userBySubject.isPresent()){
                return userBySubject.get().getId(); //get the object and then return the id
            } else {
                //else create a new user object and save it to the DB
                User user = new User();
                user.setFirstName(userInfoDTO.getGivenName());
                user.setLastName(userInfoDTO.getFamilyName());
                user.setFullName(userInfoDTO.getName());
                user.setEmailAddress(userInfoDTO.getEmail());
                user.setSub(userInfoDTO.getSub());

                return userRepository.save(user).getId();
            }

        } catch (Exception exception) {
            throw new RuntimeException("Exception occurred while registering user", exception);
        }

    }
}
