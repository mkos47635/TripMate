package com.trip.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

//Swagger-UI 확인
//http://localhost/swagger-ui.html

@Configuration
public class SwaggerConfiguration {

	@Bean
	OpenAPI openAPI() {
		
		Info info = new Info().title("SSAFY Vue+SpringBoot with JWT API 명세서")
				.description("<h3>SSAFY API Reference for Developers</h3>Swagger를 이용한 Board API<br><img src=\"/assets/img/ssafy_logo.png\" width=\"150\">")
				.version("v1")
				.contact(new io.swagger.v3.oas.models.info.Contact().name("우끼끼")
						.email("우끼@ssafy.com").url("http://edu.ssafy.com"));

		return new OpenAPI().components(new Components()).info(info);
	}

//	@Bean
//	public GroupedOpenApi publicApi() {
//		return GroupedOpenApi.builder().group("ssafy-admin").pathsToMatch("/admin/**").build();
//	}

	@Bean
	GroupedOpenApi userApi() {
		return GroupedOpenApi.builder().group("trip-all").pathsToMatch("/**").build();
	}

}