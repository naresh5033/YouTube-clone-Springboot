package com.naresh.youtubeclone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//all the properties from the user object (model
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String id;
    @JsonProperty("sub") //define the field name inside the json obj
    private String sub;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("name")
    private String name;
    @JsonProperty("picture")
    private String picture;
    private String email;
}
