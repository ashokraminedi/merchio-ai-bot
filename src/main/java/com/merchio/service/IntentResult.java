package com.merchio.service;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class IntentResult {
    private String intent;
    private double confidence;
    private Map<String, String> entities;
    private String originalPrompt;
}