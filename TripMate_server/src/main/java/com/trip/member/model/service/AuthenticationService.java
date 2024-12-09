package com.trip.member.model.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trip.common.Constants;
import com.trip.common.exception.CustomException;
import com.trip.member.model.AuthenticationRequest;
import com.trip.member.model.AuthenticationResponse;
import com.trip.member.model.MemberDto;
import com.trip.member.model.mapper.MemberMapper;
import com.trip.util.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(MemberDto request) throws Exception {
		String userId = memberMapper.getUserId(request.getUserId()).orElse(null);
		if (userId != null) {
			throw new CustomException(Constants.ExceptionType.AUTHENTICATION, HttpStatus.BAD_REQUEST,
					"User already Exists");
		}

		// 회원가입을 위해 유저를 db에 refresh-token 저장
		MemberDto member = MemberDto.builder().userId(request.getUsername()).userName(request.getUserName())
				.userPwd(passwordEncoder.encode(request.getUserPwd())).emailId(request.getEmailId())
				.emailDomain(request.getEmailDomain()).joinDate(request.getJoinDate())
				.refreshToken(jwtUtil.createRefreshToken(request.getUsername())).build();
		// 4.access-token 생성 및 반환
		memberMapper.joinMember(member);
		String accessToken = jwtUtil.createAccessToken(member.getUserId());
		String refreshToken = jwtUtil.createRefreshToken(member.getUserId());
		return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                           request.getUserId(),
                            request.getUserPwd()
                    )
            );

    			 
            // 인증 성공 시 사용자 ID 가져오기
            String userId = authentication.getName();

            // 토큰 생성
            String accessToken = jwtUtil.createAccessToken(userId);
            String refreshToken = jwtUtil.createRefreshToken(userId);

            // Refresh Token 저장
            Map<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("refreshToken", refreshToken);
            memberMapper.saveRefreshToken(map);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            throw new CustomException(Constants.ExceptionType.AUTHENTICATION, HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
	public String refresh(HttpServletRequest request, HttpServletResponse response) throws CustomException {
	        // 쿠키에서 리프레시 토큰 추출
	        String refreshToken = null;

				refreshToken = Arrays.stream(request.getCookies())
				        .filter(cookie -> "refreshToken".equals(cookie.getName()))
				        .findFirst()
				        .map(Cookie::getValue)
				        .orElseThrow(() -> new CustomException(
				                Constants.ExceptionType.INVALID_TOKEN,
				                HttpStatus.BAD_REQUEST,
				                "Refresh token is missing"
				        ));
	        // 아이디 추출
	        String userId = jwtUtil.getUserId(refreshToken);
	        
	        // 새로운 액세스 토큰 발급
	        String newAccessToken = jwtUtil.createAccessToken(userId);

	        // 응답 헤더에 액세스 토큰 추가
	        return newAccessToken;
	}
    public void logout(HttpServletResponse response) {
    	// todo 헤더에 accesstoken 만료시키기 (레디스에 블랙리스트 추가??) 
    	// and id(principal or 쿠키에서 추출해서)받아서 db에 리프레쉬토큰 삭제
//    	Map<String,String> map = new HashMap<>();
//    	map.put("userId", null)
//    	memberMapper.deleteRefreshToken(null);
        deleteCookie("refreshToken", "", response);
    }
    private void deleteCookie(String name, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
