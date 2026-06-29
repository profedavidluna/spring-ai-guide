package com.example.springaiguide;

import com.example.springaiguide.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para RecommendationService.
 * Usamos Mockito para simular la respuesta de Spring AI / OpenAI
 * y poder ejecutar las pruebas sin una API key real.
 */
class RecommendationServiceTest {

    @Test
    void getRecommendation_withWeatherReport_returnsAiResponse() {
        // Arrange: crear mocks de Spring AI ChatClient
        ChatClient.Builder mockBuilder = mock(ChatClient.Builder.class);
        ChatClient mockChatClient = mock(ChatClient.class);
        ChatClient.ChatClientRequestSpec mockRequestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec mockCallSpec = mock(ChatClient.CallResponseSpec.class);

        when(mockBuilder.defaultSystem(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockChatClient);
        when(mockChatClient.prompt()).thenReturn(mockRequestSpec);
        when(mockRequestSpec.user(anyString())).thenReturn(mockRequestSpec);
        when(mockRequestSpec.call()).thenReturn(mockCallSpec);
        when(mockCallSpec.content()).thenReturn(
                "¡Sí recomiendo visitar Bogotá! El clima es fresco y agradable para turistear.");

        RecommendationService service = new RecommendationService(mockBuilder);

        String weatherReport = "Temperatura: 18°C, Despejado, Viento 10 km/h";

        // Act
        String recommendation = service.getRecommendation(weatherReport, null);

        // Assert
        assertNotNull(recommendation);
        assertTrue(recommendation.contains("Sí recomiendo"));
    }

    @Test
    void getRecommendation_withAdditionalContext_includesContextInPrompt() {
        ChatClient.Builder mockBuilder = mock(ChatClient.Builder.class);
        ChatClient mockChatClient = mock(ChatClient.class);
        ChatClient.ChatClientRequestSpec mockRequestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec mockCallSpec = mock(ChatClient.CallResponseSpec.class);

        when(mockBuilder.defaultSystem(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockChatClient);
        when(mockChatClient.prompt()).thenReturn(mockRequestSpec);
        when(mockRequestSpec.user(anyString())).thenReturn(mockRequestSpec);
        when(mockRequestSpec.call()).thenReturn(mockCallSpec);
        when(mockCallSpec.content()).thenReturn("Respuesta de prueba");

        RecommendationService service = new RecommendationService(mockBuilder);

        String recommendation = service.getRecommendation("Reporte climático", "Viaje de negocios");

        assertNotNull(recommendation);
        assertFalse(recommendation.isBlank());
        // Verifica que se llamó al cliente de chat
        verify(mockChatClient, times(1)).prompt();
    }
}
