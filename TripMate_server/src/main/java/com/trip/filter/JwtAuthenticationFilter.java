package com.trip.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trip.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String JWT_PREFIX = "Bearer ";
        final String JWT_HEADER_KEY = "Authorization";
        final String authHeader = request.getHeader(JWT_HEADER_KEY);
        log.debug("Authorization Header: {}", authHeader);
        try {
            // Authorization 헤더 확인
            if (authHeader == null || !authHeader.startsWith(JWT_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(JWT_PREFIX.length());
            String userId = jwtUtil.getUserId(jwt); // JWT 토큰에서 유저 ID 추출
            log.debug("Extracted userId from JWT: {}", userId);
            // 유저 ID와 인증 정보 확인
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                // 토큰 유효성 검사
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    		userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // 유효하지 않은 토큰에 대해 401 Unauthorized 반환
                    log.warn("Invalid JWT Token");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token Expired");
                    return;
                }
            }
        } catch (Exception e) {
            // JWT 처리 중 예외 발생 시 401 Unauthorized 반환
            log.error("Authentication error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Expired");
            return;
        }

        // 정상적인 요청은 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
    
}