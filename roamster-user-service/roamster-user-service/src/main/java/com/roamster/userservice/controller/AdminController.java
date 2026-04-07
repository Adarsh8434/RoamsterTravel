package com.roamster.userservice.controller;

import com.roamster.userservice.dto.response.UserResponse;
import com.roamster.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}/activate")
    public UserResponse activateUser(@PathVariable Long userId) {
        return userService.activateUser(userId);
    }

    @PatchMapping("/{userId}/role")
    public UserResponse updateRole(
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {
        return userService.updateRole(userId, body.get("role"));
    }
}
