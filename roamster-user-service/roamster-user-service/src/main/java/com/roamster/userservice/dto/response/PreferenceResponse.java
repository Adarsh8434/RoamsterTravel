package com.roamster.userservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class PreferenceResponse {
    private Long id;
    private String travelStyle;
    private String budgetRange;
    private String foodPreference;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
