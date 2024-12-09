package com.trip.config;

import java.util.Arrays;
import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import lombok.extern.slf4j.Slf4j;


//import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@MapperScan(basePackages = { "com.trip.**.mapper" })
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	private final List<String> patterns = Arrays.asList("/admin/**");

//	private final JWTInterceptor jwtInterceptor;
//
//	public WebMvcConfiguration(JWTInterceptor jwtInterceptor) {
//		this.jwtInterceptor = jwtInterceptor;
//	}

//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//	    registry.addMapping("/**")
//        .allowedOrigins("http://localhost:5173") // 특정 Origin만 허용
//        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//        .allowCredentials(true) // 자격 증명 허용
//        .allowedHeaders("*")
//        .exposedHeaders("Authorization")
//        .maxAge(1800); // Preflight 캐시 기간
//	}
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(jwtInterceptor)
//		.addPathPatterns(patterns)
//        .excludePathPatterns(
//                "/swagger-ui/**",
//                "/v3/api-docs/**",
//                "/swagger-resources/**",
//                "/upload/file/**",
//                "",// 정적 리소스 경로 제외
//                "/error/**",        // Spring 에러 페이지 경로 제외
//                "/favicon.ico"      // 파비콘 요청 제외
//            );
//	}

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/")
        .setCachePeriod(3600);
//        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
}
