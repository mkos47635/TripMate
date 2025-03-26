package com.trip.openai.controller;

import com.trip.openai.model.OpenAIRequest;
import com.trip.openai.model.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping
    public String generateItinerary(@RequestBody OpenAIRequest request) {
        System.out.println("Request Data: " + request); // 요청 데이터 출력
        String response = openAIService.generateItinerary(request);
        System.out.println("111"+response);
        return response;
        //return response;
    }


}