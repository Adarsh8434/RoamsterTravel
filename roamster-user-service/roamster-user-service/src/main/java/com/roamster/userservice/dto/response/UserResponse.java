package com.roamster.userservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class UserResponse {
    private Long id;
    private String login;
    private String email;
    private String role;
    private Boolean activate;
    private LocalDateTime createdAt;
    private ProfileResponse profile;
    private PreferenceResponse preference;
}
