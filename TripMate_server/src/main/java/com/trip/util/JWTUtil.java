package com.trip.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.trip.common.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {


	@Value("${jwt.salt}")
	private String salt;

	@Value("${jwt.access-token.expiretime}")
	private long accessTokenExpireTime;

	@Value("${jwt.refresh-token.expiretime}")
	private long refreshTokenExpireTime;

	public String createAccessToken(String userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("tokenType", "ACCESS");
		return generateToken(claims, "access-token", accessTokenExpireTime);
	}

//	AccessToken에 비해 유효기간을 길게 설정.
	public String createRefreshToken(String userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("tokenType", "REFRESH");
		return generateToken(claims, "refresh-token", refreshTokenExpireTime);
	}

//	Token 발급
//		key : Claim에 셋팅될 key 값
//		value : Claim에 셋팅 될 data 값
//		subject : payload에 sub의 value로 들어갈 subject값
//		expire : 토큰 유효기간 설정을 위한 값
//		jwt 토큰의 구성 : header + payload + signature
	private String generateToken(Map<String, Object> claims, String subject, long expireTime) {
		
		Map<String, String> headers = new HashMap<>();
		headers.put("typ", "JWT");
		
//		Payload 설정 : 생성일 (IssuedAt), 유효기간 (Expiration), 
//		토큰 제목 (Subject), 데이터 (Claim) 등 정보 세팅.
		
		return Jwts.builder().header().add(headers).and().claims(claims).subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expireTime)).signWith(getSigningKey()).compact();
	}

//	Signature 설정에 들어갈 key 생성.
	private SecretKey getSigningKey() {
//			charset 설정 안하면 사용자 플랫폼의 기본 인코딩 설정으로 인코딩 됨.
		byte[] keyBytes = salt.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
//	전달 받은 토큰이 제대로 생성된 것인지 확인 하고 문제가 있다면 UnauthorizedException 발생.
	public boolean checkToken(String token) {
		try {
//			Json Web Signature? 서버에서 인증을 근거로 인증 정보를 서버의 private key 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기
			Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
//			Claims 는 Map 구현체 형태
			log.debug("claims: {}", claims);
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}
	
    // 토큰이 유효한 지 여부 확인
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = getUserId(token);
        // token으로 부터 추출한 유저네임(이메일)과 db로부터 가져온 유저네임이 같은 지 확인한다.
        final boolean isUserNameMatched = userId.equals(userDetails.getUsername());

        return isUserNameMatched && !isTokenExpired(token);
    }
    // 토큰이 만료되었는 지 확인
    private boolean isTokenExpired(String token) {
        // 현재 날짜보다 만료일이 앞에 있다면 만료되었다고 판단한다.
        return extractExpiration(token).before(new Date());
    }
    // 토큰으로부터 토큰 만료일을 가져온다.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // 토큰으로부터 모든 정보를 가져온다
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey()) /* jwt가 중간에 변경되지 않았는 지 확인하기 위한 서명키 */
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
    *
    * @param token jwt 토큰
    * @param claimsResolver jwt 토큰으로 부터 어떤 정보를 추출할 지 지정하는 함수
    * @return 토큰으로 부터 추출한 정보
    * @param <T> 토큰으로 부터 추출한 정보의 타입
    */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
   
	// token으로 부터 userId를 추출한다.
	public String getUserId(String token) {
		Claims claims = null;
		try {
			claims = extractAllClaims(token);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorizedException();
		}
		Map<String, Object> value = claims;
		log.info("value : {}", value);
		return (String) value.get("userId");
	}

}