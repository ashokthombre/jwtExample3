package com.jwt.example.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
