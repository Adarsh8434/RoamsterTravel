package com.roamster.userservice.controller;

import com.roamster.userservice.dto.request.*;
import com.roamster.userservice.dto.response.TokenResponse;
import com.roamster.userservice.dto.response.UserResponse;
import com.roamster.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody Map<String, String> body) {
        return userService.refresh(body.get("refreshToken"));
    }

    // POST /api/users/forgot-password
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest req) {
        userService.forgotPassword(req);
        return ResponseEntity.ok("OTP sent to your email");
    }

    // POST /api/users/verify-otp
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest req) {
        userService.verifyOtp(req);
        return ResponseEntity.ok("OTP verified successfully");
    }

    // POST /api/users/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest req) {
        userService.resetPassword(req);
        return ResponseEntity.ok("Password reset successfully");
    }
}
