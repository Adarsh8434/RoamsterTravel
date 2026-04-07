package com.roamster.userservice.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String phoneNumber;
    private Integer age;
    private String gender;   // MALE | FEMALE | OTHER
    private String city;
}
