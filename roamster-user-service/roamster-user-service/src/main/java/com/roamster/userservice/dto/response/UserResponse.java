package com.roamster.userservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    Long id;
    String login;
    private String email;
    private String role;
    private Boolean activate;
    private LocalDateTime createdAt;
    private ProfileResponse profile;
    private PreferenceResponse preference;
}
