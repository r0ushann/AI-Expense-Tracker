package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AiCategorizationResult;
import dto.claude.ClaudeRequest;
import dto.claude.ClaudeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ClaudeAiService {

    private static final Logger log = LoggerFactory.getLogger(ClaudeAiService.class);

    @Value("${anthropic.api.key}")
    private String apiKey;

    @Value("${anthropic.api.url}")
    private String apiUrl;

    @Value("${anthropic.api.model}")
    private String model;

    @Value("${anthropic.api.max-tokens}")
    private Integer maxTokens;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ClaudeAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public AiCategorizationResult categorizeExpense(String description, String amount) {
        log.info("Categorizing expense: {} - ${}", description, amount);

        try {
            String prompt = buildCategorizationPrompt(description, amount);
            ClaudeRequest request = buildClaudeRequest(prompt);

            ClaudeResponse response = callClaudeApi(request);

            return parseCategorizationResponse(response);
        } catch (Exception e) {
            log.error("Error categorizing expense with AI: {}", e.getMessage(), e);
            return getFallbackCategorization(description);
        }
    }

    private String buildCategorizationPrompt(String description, String amount) {
        return String.format("""
            Analyze this expense and categorize it. Return ONLY a JSON object with no additional text.
            
            Expense Description: %s
            Amount: $%s
            
            Categorize this expense into one of these categories:
            - Food & Dining (restaurants, groceries, coffee shops)
            - Transportation (fuel, public transit, ride-sharing, parking)
            - Shopping (clothing, electronics, online shopping)
            - Entertainment (movies, concerts, games, subscriptions)
            - Bills & Utilities (electricity, water, internet, phone)
            - Healthcare (doctor visits, pharmacy, insurance)
            - Travel (hotels, flights, vacation expenses)
            - Education (courses, books, tuition)
            - Personal Care (salon, gym, spa)
            - Home & Garden (furniture, repairs, gardening)
            - Gifts & Donations
            - Other
            
            Return ONLY this JSON structure:
            {
              "category": "Primary Category",
              "subCategory": "Specific subcategory if applicable",
              "merchantName": "Extracted merchant name if identifiable",
              "confidenceScore": 0.95,
              "reasoning": "Brief explanation"
            }
            """, description, amount);
    }

    private ClaudeRequest buildClaudeRequest(String prompt) {
        ClaudeRequest.Message message = new ClaudeRequest.Message("user", prompt);

        return new ClaudeRequest(
                model,
                maxTokens,
                Collections.singletonList(message)
        );
    }

    private ClaudeResponse callClaudeApi(ClaudeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        HttpEntity<ClaudeRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ClaudeResponse> response = restTemplate.postForEntity(
                apiUrl,
                entity,
                ClaudeResponse.class
        );

        return response.getBody();
    }

    private AiCategorizationResult parseCategorizationResponse(ClaudeResponse response) {
        if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
            throw new RuntimeException("Empty response from Claude API");
        }

        String responseText = response.getContent().get(0).getText();
        log.debug("Claude response: {}", responseText);

        try {
            // Extract JSON from response (Claude might add some text before/after)
            String jsonText = extractJson(responseText);
            JsonNode jsonNode = objectMapper.readTree(jsonText);

            AiCategorizationResult result = new AiCategorizationResult();
            result.setCategory(jsonNode.get("category").asText());
            result.setSubCategory(jsonNode.has("subCategory") ? jsonNode.get("subCategory").asText() : null);
            result.setMerchantName(jsonNode.has("merchantName") ? jsonNode.get("merchantName").asText() : null);
            result.setConfidenceScore(jsonNode.has("confidenceScore") ? jsonNode.get("confidenceScore").asDouble() : 0.5);
            result.setReasoning(jsonNode.has("reasoning") ? jsonNode.get("reasoning").asText() : "");

            return result;
        } catch (JsonProcessingException e) {
            log.error("Error parsing Claude response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    private String extractJson(String text) {
        // Find JSON object in the response
        int startIndex = text.indexOf("{");
        int endIndex = text.lastIndexOf("}");

        if (startIndex >= 0 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1);
        }

        return text;
    }

    private AiCategorizationResult getFallbackCategorization(String description) {
        log.warn("Using fallback categorization for: {}", description);

        AiCategorizationResult result = new AiCategorizationResult();
        result.setCategory("Other");
        result.setSubCategory("Uncategorized");
        result.setMerchantName(null);
        result.setConfidenceScore(0.0);
        result.setReasoning("AI categorization failed, manual review needed");

        return result;
    }
}