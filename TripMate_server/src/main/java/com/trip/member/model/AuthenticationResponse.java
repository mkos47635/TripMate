package com.trip.member.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	private String accessToken;
	private String refreshToken;
}
