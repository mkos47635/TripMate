package com.trip.attraction.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trip.attraction.model.AttractionDto;
import com.trip.attraction.model.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/attraction")
public class AttractionController {
	
	
	private final AttractionService attractionService;

	public AttractionController(AttractionService attractionService) {
		this.attractionService = attractionService;
	}
	
	@Operation(summary = "Attraction목록", description = "Attraction 정보를 반환한다.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Attraction목록 OK!!"),
			@ApiResponse(responseCode = "404", description = "페이지없어!!"),
			@ApiResponse(responseCode = "500", description = "서버에러!!") })
	@GetMapping("/list")
	public ResponseEntity<?> listAttraction(
			@RequestParam @Parameter(description = "Attraction을 얻기위한 부가정보.", required = true) Map<String, String> map) {
		log.info("Attraction map - {}", map);
		try {
			List<AttractionDto> attractionList =  attractionService.searchAttraction(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(attractionList);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@Operation(summary = "gugunCode get", description = "sidoCode를 통해 gugunCode를 반환한다.")
	@GetMapping("/gugun")
	public ResponseEntity<?> getGugun(@RequestParam("sidoCode") int sidoCode) {
		log.info("선택된 sidoCode: {}", sidoCode);
	    try {
	        List<String> gugunList = attractionService.getGugun(sidoCode);
	        HttpHeaders header = new HttpHeaders();
	        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
	        return ResponseEntity.ok().headers(header).body(gugunList);
	    } catch (Exception e) {
	        return exceptionHandling(e);
	    }
	}

	
	

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
