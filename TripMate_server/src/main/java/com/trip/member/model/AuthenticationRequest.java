package com.trip.member.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "AuthenticationRequest (로그인 요청 정보)", description = "로그인 시 필요한 회원의 아이디와 비밀번호를 가진 클래스")
public class AuthenticationRequest {

    @Schema(description = "회원아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "회원비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPwd;

    // Getter, Setter 메소드
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

	@Override
	public String toString() {
		return "AuthenticationRequest [userId=" + userId + ", userPwd=" + userPwd + "]";
	}
    
}
