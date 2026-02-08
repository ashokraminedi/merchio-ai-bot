package com.merchio.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class IntentDetectionService {

    @Value("${merchio.chatbot.rules-file}")
    private Resource rulesFile;

    private JsonNode rulesConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadRules() throws IOException {
        rulesConfig = objectMapper.readTree(rulesFile.getInputStream());
        log.info("🛍️ Merchio chatbot rules loaded successfully");
    }

    public IntentResult detectIntent(String userPrompt) {
        String normalizedPrompt = userPrompt.toLowerCase().trim();

        // Validate prompt
        if (!validatePrompt(normalizedPrompt)) {
            return IntentResult.builder()
                    .intent("INVALID_PROMPT")
                    .confidence(1.0)
                    .build();
        }

        JsonNode intentRules = rulesConfig.get("intentRules");
        Map<String, Double> intentScores = new HashMap<>();

        for (JsonNode rule : intentRules) {
            String intent = rule.get("intent").asText();
            JsonNode keywords = rule.get("keywords");

            double score = calculateIntentScore(normalizedPrompt, keywords);
            intentScores.put(intent, score);
        }

        // Find highest scoring intent
        String detectedIntent = intentScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("GENERAL_INQUIRY");

        double confidence = intentScores.get(detectedIntent);

        // Extract entities
        Map<String, String> entities = extractEntities(normalizedPrompt, detectedIntent);

        log.debug("Intent detected: {} (confidence: {})", detectedIntent, confidence);

        return IntentResult.builder()
                .intent(detectedIntent)
                .confidence(confidence)
                .entities(entities)
                .originalPrompt(userPrompt)
                .build();
    }

    private double calculateIntentScore(String prompt, JsonNode keywords) {
        if (keywords.isEmpty()) {
            return 0.0;
        }

        int matchCount = 0;
        for (JsonNode keyword : keywords) {
            if (prompt.contains(keyword.asText().toLowerCase())) {
                matchCount++;
            }
        }

        return (double) matchCount / keywords.size();
    }

    private boolean validatePrompt(String prompt) {
        JsonNode validationRules = rulesConfig.get("validationRules");
        int maxLength = validationRules.get("maxPromptLength").asInt();
        int minLength = validationRules.get("minPromptLength").asInt();

        return prompt.length() >= minLength && prompt.length() <= maxLength;
    }

    private Map<String, String> extractEntities(String prompt, String intent) {
        Map<String, String> entities = new HashMap<>();

        // Extract order number
        Pattern orderPattern = Pattern.compile("(?:order|#)\\s*([A-Z0-9]{6,12})", Pattern.CASE_INSENSITIVE);
        Matcher orderMatcher = orderPattern.matcher(prompt);
        if (orderMatcher.find()) {
            entities.put("orderNumber", orderMatcher.group(1).toUpperCase());
        }

        // Extract email
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");
        Matcher emailMatcher = emailPattern.matcher(prompt);
        if (emailMatcher.find()) {
            entities.put("email", emailMatcher.group());
        }

        // Extract product name (simplified)
        if (intent.equals("PRODUCT_SEARCH") || intent.equals("CATALOG_AVAILABILITY")) {
            String productHint = prompt.replaceAll("(?i)(search|find|looking for|buy|available|stock|in|is|the|a|an)", "").trim();
            if (!productHint.isEmpty()) {
                entities.put("productName", productHint);
            }
        }

        return entities;
    }
}