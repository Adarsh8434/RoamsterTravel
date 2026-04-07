package com.roamster.userservice.controller;

import com.roamster.userservice.dto.request.PreferenceUpdateRequest;
import com.roamster.userservice.dto.response.PreferenceResponse;
import com.roamster.userservice.model.User;
import com.roamster.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final UserService userService;

    @GetMapping("/me")
    public PreferenceResponse getMyPreferences(@AuthenticationPrincipal User user) {
        return userService.getPreference(user.getId());
    }

    @PatchMapping("/me")
    public PreferenceResponse updateMyPreferences(
            @AuthenticationPrincipal User user,
            @RequestBody PreferenceUpdateRequest request) {
        return userService.updatePreference(user.getId(), request);
    }
}
