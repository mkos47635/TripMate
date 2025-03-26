package com.trip.openai.model;

import lombok.Data;

@Data
public class OpenAIRequest {
    private String model;        // JSON: "model"
    private String prompt;       // JSON: "prompt"
    private int maxTokens;       // JSON: "maxTokens"
    private double temperature;  // JSON: "temperature"
}
