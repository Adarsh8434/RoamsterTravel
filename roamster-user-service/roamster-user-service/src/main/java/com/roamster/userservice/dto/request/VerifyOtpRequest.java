package com.roamster.userservice.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequest{

    private String email;

    private String otp;

}