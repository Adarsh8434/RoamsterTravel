package com.roamster.userservice.controller;

import com.roamster.userservice.dto.request.ProfileUpdateRequest;
import com.roamster.userservice.dto.response.ProfileResponse;
import com.roamster.userservice.model.User;
import com.roamster.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/me")
    public ProfileResponse getMyProfile(@AuthenticationPrincipal User user) {
        return userService.getProfile(user.getId());
    }

    @PatchMapping("/me")
    public ProfileResponse updateMyProfile(
            @AuthenticationPrincipal User user,
            @RequestBody ProfileUpdateRequest request) {
        return userService.updateProfile(user.getId(), request);
    }
}
