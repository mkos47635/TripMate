package com.trip.member.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Schema(title = "MemberDto (회원정보)", description = "회원의 아이디, 비번, 이름을 가진 Domain Class")
public class MemberDto implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "회원아이디", requiredMode = Schema.RequiredMode.REQUIRED)
	private String userId;
	@Schema(description = "회원이름")
	private String userName;
	@Schema(description = "회원비밀번호")
	private String userPwd;
	@Schema(description = "이메일아이디")
	private String emailId;
	@Schema(description = "이메일도메인", defaultValue = "ssafy.com")
	private String emailDomain;
	@Schema(description = "가입일", defaultValue = "현재시간")
	private String joinDate;
	@Schema(description = "refreshToken", defaultValue = "")
	private String refreshToken;
	
    @Schema(description = "회원 권한")
    private String role; // 사용자 권한 (예: ROLE_USER, ROLE_ADMIN)
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		 return List.of(new SimpleGrantedAuthority(role.name()));
		return Collections.singletonList(new SimpleGrantedAuthority(role != null ? role : "ROLE_USER"));
	}
	public String getUserName() {
		return userName;
	}
	@Override
	public String getPassword() {
		return userPwd;
	}

	@Override
	public String getUsername() {
		return userId;
	}
    @Override
    public boolean isAccountNonExpired() {
        // 계정의 만료 여부를 설정 (true로 설정하여 만료되지 않음)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정의 잠금 여부를 설정 (true로 설정하여 잠금되지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호의 만료 여부를 설정 (true로 설정하여 만료되지 않음)
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부를 설정 (true로 설정하여 활성화)
        return true;
    }



}
