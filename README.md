# Spring AI Guide — Taller práctico

Guía práctica para aprender **Spring AI** conectando distintos modelos de inteligencia artificial desde Spring Boot.

---

## Rama actual: `copilot/connect-openai-chatbot`

### ¿Qué hace este proyecto?

Un chatbot de recomendaciones de viaje que:

1. Recibe el nombre de un destino turístico.
2. Consulta la **API gratuita de Open-Meteo** (sin API key) para obtener el clima actual y el pronóstico de 7 días.
3. Envía ese reporte a **OpenAI** usando **Spring AI** para que la IA recomiende si vale la pena visitar ese destino.

---

## Requisitos

- Java 17+
- Maven 3.9+
- Una API key de OpenAI → [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)

---

## Configuración

Configura tu API key de OpenAI como variable de entorno:

```bash
export SPRING_AI_OPENAI_API_KEY=sk-...
```

O edita `src/main/resources/application.properties`:

```properties
spring.ai.openai.api-key=sk-...
```

---

## Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

---

## Endpoints

### POST `/api/recommend`

Cuerpo JSON:

```json
{
  "location": "Cartagena de Indias",
  "additionalContext": "Viaje familiar con niños, verano"
}
```

### GET `/api/recommend`

Para pruebas rápidas desde el navegador:

```
GET http://localhost:8080/api/recommend?location=Bogota&context=fin+de+semana
```

### Ejemplo de respuesta

```json
{
  "location": "Cartagena de Indias",
  "weatherSummary": "=== REPORTE METEOROLÓGICO ===\nDestino: Cartagena, Colombia\n...",
  "aiRecommendation": "¡Sí recomiendo visitar Cartagena! El clima es cálido y soleado..."
}
```

---

## Arquitectura

```
ChatbotController  →  WeatherService  →  Open-Meteo API (gratuita, sin API key)
                   →  RecommendationService  →  OpenAI (via Spring AI)
```

| Clase | Responsabilidad |
|---|---|
| `ChatbotController` | Expone los endpoints REST |
| `WeatherService` | Geocodificación + pronóstico meteorológico |
| `RecommendationService` | Envía el reporte a OpenAI y retorna la recomendación |

---

## Tecnologías clave

| Tecnología | Versión | Uso |
|---|---|---|
| Spring Boot | 3.3.4 | Framework base |
| Spring AI | 1.0.0 | Integración con OpenAI |
| Open-Meteo | — | API meteorológica gratuita |
| OpenAI GPT-4o-mini | — | Modelo de IA para recomendaciones |

---

## Ejecutar los tests

```bash
mvn test
```
