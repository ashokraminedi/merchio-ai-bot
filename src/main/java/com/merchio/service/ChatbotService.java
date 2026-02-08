package com.merchio.service;

import com.merchio.ai.AIProvider;
import com.merchio.ai.ChatMessage;
import com.merchio.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final AIProvider aiProvider;
    private final IntentDetectionService intentDetectionService;
    private final OrderService orderService;
    private final ProductService productService;

    public ChatbotResponse processMessage(String userMessage, List<ChatMessage> conversationHistory) {
        log.info("🛍️ Merchio processing message: {}", userMessage);

        // Step 1: Detect intent
        IntentResult intentResult = intentDetectionService.detectIntent(userMessage);
        log.info("Intent: {} (confidence: {})", intentResult.getIntent(), intentResult.getConfidence());

        // Step 2: Gather context from database
        String contextInfo = gatherContextInfo(intentResult);

        // Step 3: Build system prompt
        String systemPrompt = buildSystemPrompt(intentResult, contextInfo);

        // Step 4: Prepare conversation
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.builder().role("system").content(systemPrompt).build());

        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            messages.addAll(conversationHistory);
        }

        messages.add(ChatMessage.builder().role("user").content(userMessage).build());

        // Step 5: Generate AI response
        String aiResponse = aiProvider.generateResponse(systemPrompt, messages);

        log.info("✅ Response generated via {}", aiProvider.getProviderName());

        return ChatbotResponse.builder()
                .message(aiResponse)
                .intent(intentResult.getIntent())
                .confidence(intentResult.getConfidence())
                .entities(intentResult.getEntities())
                .provider(aiProvider.getProviderName())
                .build();
    }

    private String gatherContextInfo(IntentResult intentResult) {
        String intent = intentResult.getIntent();
        Map<String, String> entities = intentResult.getEntities();

        StringBuilder context = new StringBuilder();

        switch (intent) {
            case "ORDER_STATUS":
                if (entities.containsKey("orderNumber")) {
                    String orderInfo = orderService.getOrderStatusInfo(entities.get("orderNumber"));
                    context.append("Order Information:\n").append(orderInfo);
                } else {
                    context.append("No order number provided in the query.");
                }
                break;

            case "PRODUCT_SEARCH":
                if (entities.containsKey("productName")) {
                    List<Product> products = productService.searchProducts(entities.get("productName"));
                    context.append("Product Search Results:\n")
                            .append(productService.formatProductList(products));
                }
                break;

            case "CATALOG_AVAILABILITY":
                if (entities.containsKey("productName")) {
                    String availability = productService.checkAvailability(entities.get("productName"));
                    context.append("Product Availability:\n").append(availability);
                }
                break;

            default:
                context.append("No specific context required for this query.");
        }

        return context.toString();
    }

    private String buildSystemPrompt(IntentResult intentResult, String contextInfo) {
        return String.format("""
            You are Merchio, an intelligent shopping companion and customer service chatbot for an e-commerce platform.
            
            Your capabilities:
            - Track orders and provide shipping updates
            - Search products and check inventory
            - Assist with payments and refunds
            - Answer general shopping questions
            
            Current Query Intent: %s
            Confidence Level: %.2f
            
            Relevant Database Information:
            %s
            
            Instructions:
            1. Be friendly, helpful, and conversational
            2. Use the provided database information to give accurate answers
            3. If information is missing, politely ask for clarification
            4. Never fabricate order numbers or product details
            5. If you cannot help, suggest contacting customer support
            6. Keep responses concise and actionable
            
            Brand Voice: Professional yet friendly, like a knowledgeable shopping assistant.
            
            Respond naturally to the user's question based on the context provided.
            """,
                intentResult.getIntent(),
                intentResult.getConfidence(),
                contextInfo
        );
    }
}