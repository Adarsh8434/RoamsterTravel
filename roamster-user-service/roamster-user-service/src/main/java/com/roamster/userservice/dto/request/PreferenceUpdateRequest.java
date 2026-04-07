package com.roamster.userservice.dto.request;

import lombok.Data;

@Data
public class PreferenceUpdateRequest {
    private String travelStyle;      // RELAXED | ADVENTURE | AESTHETIC
    private String budgetRange;      // BASIC | MODERATE | PREMIUM
    private String foodPreference;   // VEG | NON_VEG | VEGAN | JAIN
}
