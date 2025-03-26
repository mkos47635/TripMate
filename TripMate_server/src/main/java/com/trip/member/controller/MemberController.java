package com.trip.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.common.exception.CustomException;
import com.trip.member.model.AuthenticationRequest;
import com.trip.member.model.AuthenticationResponse;
import com.trip.member.model.MemberDto;
import com.trip.member.model.service.AuthenticationService;
import com.trip.member.model.service.MemberService;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationService authenticationService;
	
	@Operation(summary = "회원등록", description = "회원의 정보를 받아 데이터베이스에 등록.")
	@PostMapping(value = "/join")
	public ResponseEntity<?> join(
			@RequestBody(description = "등록할 회원정보.", required = true, content = @Content(schema = @Schema(implementation = MemberDto.class))) @org.springframework.web.bind.annotation.RequestBody MemberDto memberDto,
			 HttpServletResponse response) {
		System.out.println(memberDto);
		log.debug("userRegister memberDto : {}", memberDto);
		try {
			AuthenticationResponse authResponse = authenticationService.register(memberDto);
		    // 리프레시 토큰을 쿠키에 저장
		    setRefreshTokenInCookie(response, authResponse.getRefreshToken());
	        // 응답 본문에 액세스 토큰 추가
	        Map<String, String> responseBody = new HashMap<>();
	        responseBody.put("accessToken", authResponse.getAccessToken());
	        return ResponseEntity.ok(responseBody);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
    // 로그인
	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @org.springframework.web.bind.annotation.RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
	    try {
	        AuthenticationResponse authResponse = authenticationService.authenticate(request);

	        // 리프레시 토큰을 쿠키에 저장
	        setRefreshTokenInCookie(response, authResponse.getRefreshToken());

	        return ResponseEntity.ok(Map.of("accessToken", authResponse.getAccessToken()));
	    } catch (Exception e) {
	        return exceptionHandling(e);
	    }
    }
    @GetMapping("/idCheck/{checkId}")
    private int idCheck(@PathVariable("checkId") String checkId) throws Exception {
            return memberService.idCheck(checkId);
    }

	@Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws CustomException {
	    try {
	    	String newAccessToken = authenticationService.refresh(request, response);
	        return ResponseEntity.ok(Map.of("accessToken", newAccessToken)); // JSON 응답
	    } catch (Exception e) {
	        log.error("Access Token 재발급 실패: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Map.of(
	                        "statusCode", HttpStatus.FORBIDDEN.value(),
	                        "message", "refreshToken 만료 403에러"
	                ));
	    }
	}

	@Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
	    try {
	        authenticationService.logout(response);
	        return ResponseEntity.ok().body("로그아웃이 성공적으로 처리되었습니다.");
	    } catch (Exception e) {
	        // 로그아웃 실패 시 500 상태 코드와 에러 메시지 반환
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("로그아웃 처리 중 오류가 발생했습니다.");
	    }
	}
	@GetMapping("/myPage/{loginId}")
	public ResponseEntity<?> userInfo(@PathVariable("loginId") String userId){
		try {
			return ResponseEntity.ok().body(memberService.getMember(userId));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("유저정보 처리중 오류.."); 
		}
	}
	@PatchMapping("/update")
	public ResponseEntity<?> updateUser(@org.springframework.web.bind.annotation.RequestBody MemberDto memberDto){
        try {
            // 유저 정보를 업데이트하는 서비스 호출
        	memberService.modifyMember(memberDto);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("회원 정보를 수정하는데 실패했습니다.");
        }
	}
	private void setRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
	    Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
	    refreshTokenCookie.setHttpOnly(true); // JavaScript에서 접근 불가
//	    refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
	    refreshTokenCookie.setPath("/"); // 전체 도메인 경로에서 사용 가능
	    refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 유효 기간 7일
//	    refreshTokenCookie.setSameSite("Strict"); // 크로스 사이트 요청 제한
	    response.addCookie(refreshTokenCookie);
	}
    
	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
//		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
	}
}
