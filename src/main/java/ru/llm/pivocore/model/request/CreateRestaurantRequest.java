package ru.llm.pivocore.model.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRestaurantRequest {

    @JsonAlias("name")
    private String name;

    @JsonAlias("location")
    private String location;

    @JsonAlias("website")
    private String website;

    @JsonAlias("phone_number")
    private String phoneNumber;

    @JsonAlias("email")
    private String email;

    @JsonAlias("cuisines")
    private List<Long> cuisines;

}
