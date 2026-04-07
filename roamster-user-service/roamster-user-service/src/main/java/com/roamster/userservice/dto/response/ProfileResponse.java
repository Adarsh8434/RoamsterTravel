package com.roamster.userservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class ProfileResponse {
    private Long id;
    private String phoneNumber;
    private Integer age;
    private String gender;
    private String city;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
