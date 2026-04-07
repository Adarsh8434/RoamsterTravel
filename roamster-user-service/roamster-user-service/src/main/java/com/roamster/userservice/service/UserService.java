package com.roamster.userservice.service;

import com.roamster.userservice.dto.request.*;
import com.roamster.userservice.dto.response.*;
import com.roamster.userservice.exception.*;
import com.roamster.userservice.model.*;
import com.roamster.userservice.repository.*;
import com.roamster.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Set<String> VALID_ROLES = Set.of("USER", "ADMIN", "CREATOR", "PARTNER");

    private final UserRepository userRepo;
    private final UserProfileRepository profileRepo;
    private final UserPreferenceRepository preferenceRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ── Auth ───────────────────────────────────────────────────

    @Transactional
    public UserResponse register(RegisterRequest req) {
        if (userRepo.existsByLogin(req.getLogin()))
            throw new ConflictException("Login already in use");
        if (userRepo.existsByEmail(req.getEmail()))
            throw new ConflictException("Email already in use");

        User user = User.builder()
                .login(req.getLogin())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role("USER")
                .activate(false)
                .build();
        userRepo.save(user);

        // Auto-create empty profile and preference
        profileRepo.save(UserProfile.builder().user(user).build());
        preferenceRepo.save(UserPreference.builder().user(user).build());

        return toUserResponse(user);
    }

    public TokenResponse login(LoginRequest req) {
        User user = userRepo.findByLoginOrEmail(req.getLogin(), req.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new UnauthorizedException("Invalid credentials");

        if (!user.getActivate())
            throw new ForbiddenException("Account is not activated yet");

        return TokenResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getId()))
                .tokenType("Bearer")
                .build();
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtUtil.isValid(refreshToken) || !"refresh".equals(jwtUtil.getTokenType(refreshToken)))
            throw new UnauthorizedException("Invalid refresh token");

        Long userId = jwtUtil.getUserId(refreshToken);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return TokenResponse.builder()
                .accessToken(jwtUtil.generateAccessToken(user.getId(), user.getRole()))
                .refreshToken(jwtUtil.generateRefreshToken(user.getId()))
                .tokenType("Bearer")
                .build();
    }

    // ── Profile ────────────────────────────────────────────────

    public ProfileResponse getProfile(Long userId) {
        UserProfile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return toProfileResponse(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest req) {
        UserProfile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if (req.getPhoneNumber() != null) profile.setPhoneNumber(req.getPhoneNumber());
        if (req.getAge() != null)         profile.setAge(req.getAge());
        if (req.getGender() != null)      profile.setGender(req.getGender());
        if (req.getCity() != null)        profile.setCity(req.getCity());

        return toProfileResponse(profileRepo.save(profile));
    }

    // ── Preferences ────────────────────────────────────────────

    public PreferenceResponse getPreference(Long userId) {
        UserPreference pref = preferenceRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found"));
        return toPreferenceResponse(pref);
    }

    @Transactional
    public PreferenceResponse updatePreference(Long userId, PreferenceUpdateRequest req) {
        UserPreference pref = preferenceRepo.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Preference not found"));

        if (req.getTravelStyle() != null)    pref.setTravelStyle(req.getTravelStyle());
        if (req.getBudgetRange() != null)    pref.setBudgetRange(req.getBudgetRange());
        if (req.getFoodPreference() != null) pref.setFoodPreference(req.getFoodPreference());

        return toPreferenceResponse(preferenceRepo.save(pref));
    }

    // ── Admin ──────────────────────────────────────────────────

    @Transactional
    public UserResponse activateUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActivate(true);
        return toUserResponse(userRepo.save(user));
    }

    @Transactional
    public UserResponse updateRole(Long userId, String role) {
        if (!VALID_ROLES.contains(role))
            throw new IllegalArgumentException("Invalid role. Must be one of: " + VALID_ROLES);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(role);
        return toUserResponse(userRepo.save(user));
    }

    public UserResponse getUserById(Long userId) {
        return toUserResponse(userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    // ── Mappers ────────────────────────────────────────────────

    private UserResponse toUserResponse(User u) {
        UserProfile profile = profileRepo.findByUserId(u.getId()).orElse(null);
        UserPreference pref = preferenceRepo.findByUserId(u.getId()).orElse(null);
        return UserResponse.builder()
                .id(u.getId())
                .login(u.getLogin())
                .email(u.getEmail())
                .role(u.getRole())
                .activate(u.getActivate())
                .createdAt(u.getCreatedAt())
                .profile(profile != null ? toProfileResponse(profile) : null)
                .preference(pref != null ? toPreferenceResponse(pref) : null)
                .build();
    }

    private ProfileResponse toProfileResponse(UserProfile p) {
        return ProfileResponse.builder()
                .id(p.getId())
                .phoneNumber(p.getPhoneNumber())
                .age(p.getAge())
                .gender(p.getGender())
                .city(p.getCity())
                .userId(p.getUser().getId())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private PreferenceResponse toPreferenceResponse(UserPreference p) {
        return PreferenceResponse.builder()
                .id(p.getId())
                .travelStyle(p.getTravelStyle())
                .budgetRange(p.getBudgetRange())
                .foodPreference(p.getFoodPreference())
                .userId(p.getUser().getId())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
