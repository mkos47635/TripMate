package com.trip.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trip.member.model.MemberDto;
import com.trip.member.model.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfigure {
	
	private final MemberMapper memberMapper;

	@Bean
	UserDetailsService userDetailsService() {
	    return userId -> {
	        try {
	            Optional<MemberDto> user = memberMapper.getMember(userId);
	            return user.map(u -> org.springframework.security.core.userdetails.User.builder()
	                    .username(u.getUserId())
	                    .password(u.getUserPwd())
	                    .build())
	                .orElseThrow(() -> new UsernameNotFoundException("User not found for ID: " + userId));
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new UsernameNotFoundException("Error retrieving user with ID: " + userId, e);
	        }
	    };
	}

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    // 인증 책임자
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

	
	
}
