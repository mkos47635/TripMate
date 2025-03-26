package com.trip.openai.model.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.openai.model.Message;
import com.trip.openai.model.OpenAIRequest;
import com.trip.openai.model.OpenAIRequestBody;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAIService {

    private static final String OPENAI_API_URL = "";
    private static final String API_KEY = "";

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateItinerary(OpenAIRequest request) {
        try {
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);
            headers.set("Content-Type", "application/json");

            // 요청 바디 구성
            OpenAIRequestBody requestBody = new OpenAIRequestBody(
                request.getModel(),
                List.of(new Message("user", request.getPrompt())),
                request.getMaxTokens(),
                request.getTemperature()
            );

            // JSON 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

            System.out.println("Request Body: " + jsonRequestBody); // 디버깅용 출력

            HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);

            // OpenAI API 호출
            ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, entity, String.class);

            // JSON 파싱
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            return content; // 추출한 일정 반환
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate itinerary", e);
        }
    }
}
