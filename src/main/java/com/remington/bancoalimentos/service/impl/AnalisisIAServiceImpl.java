package com.remington.bancoalimentos.service.impl;

import com.remington.bancoalimentos.model.entity.Inventario;
import com.remington.bancoalimentos.model.repository.DonacionRepository;
import com.remington.bancoalimentos.model.repository.EntregaRepository;
import com.remington.bancoalimentos.model.repository.InventarioRepository;
import com.remington.bancoalimentos.service.AnalisisIAService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Implementación del servicio de analítica predictiva y asistente inteligente mediante la API de Groq Cloud.
 * <p>
 * Se comunica de forma segura mediante HTTPS con el endpoint de inferencia de Groq, empleando
 * el modelo de lenguaje de código abierto {@code openai/gpt-oss-120b} para generar diagnósticos
 * fundamentados en el estado operacional real de la base de datos MySQL (existencias, donaciones y entregas).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Service
public class AnalisisIAServiceImpl implements AnalisisIAService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.api.model}")
    private String modelName;

    private final InventarioRepository inventarioRepository;
    private final DonacionRepository donacionRepository;
    private final EntregaRepository entregaRepository;
    private final RestTemplate restTemplate;

    /**
     * Constructor con inyección de dependencias para los repositorios de datos del sistema.
     *
     * @param inventarioRepository Repositorio de existencias físicas en bodega.
     * @param donacionRepository   Repositorio para conteo y trazabilidad de donaciones.
     * @param entregaRepository    Repositorio para conteo de despachos comunitarios.
     */
    public AnalisisIAServiceImpl(InventarioRepository inventarioRepository,
                                 DonacionRepository donacionRepository,
                                 EntregaRepository entregaRepository) {
        this.inventarioRepository = inventarioRepository;
        this.donacionRepository = donacionRepository;
        this.entregaRepository = entregaRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Construye un contexto dinámico a partir del inventario disponible en MySQL y formula un prompt
     * especializado para que el modelo de IA genere el diagnóstico predictivo de mermas y rotación.
     *
     * @return Diagnóstico predictivo estructurado en viñetas profesionales.
     */
    @Override
    public String generarAnalisisPredictivo() {
        List<Inventario> inventarios = inventarioRepository.findAll();
        long totalDonaciones = donacionRepository.count();
        long totalEntregas = entregaRepository.count();

        StringBuilder contexto = new StringBuilder();
        contexto.append("Contexto de operación del Banco de Alimentos de Medellín:\n");
        contexto.append("- Total de donaciones históricas registradas: ").append(totalDonaciones).append("\n");
        contexto.append("- Total de despachos comunitarios a estratos 1 a 3: ").append(totalEntregas).append("\n");
        contexto.append("- Estado actual del inventario en bodega central:\n");
        for (Inventario inv : inventarios) {
            contexto.append("  * ").append(inv.getProducto().getNombreProducto())
                    .append(" (").append(inv.getProducto().getCategoria()).append("): ")
                    .append(inv.getCantidadDisponible()).append(" ")
                    .append(inv.getProducto().getUnidadMedida()).append("\n");
        }

        String prompt = "Actúa como el Asesor Experto en Inteligencia Artificial y Logística del Banco de Alimentos Inteligente para Medellín. " +
                "Con base en los datos reales del inventario que te proporciono, realiza un análisis predictivo y estratégico breve y contundente:\n" +
                "1. Diagnóstico de disponibilidad actual (¿qué productos presentan niveles críticos o riesgo de agotamiento?).\n" +
                "2. Estimación predictiva de demanda para las comunas vulnerables (estratos 1 a 3).\n" +
                "3. Recomendación logística prioritaria para la próxima campaña de recolección con supermercados aliados.\n" +
                "Responde en español, con lenguaje profesional y estructurado con viñetas.\n\n" + contexto.toString();

        return llamarGroq(prompt);
    }

    /**
     * Envía una consulta en lenguaje natural formulada por el usuario hacia el modelo Groq.
     *
     * @param pregunta Interrogante o solicitud técnica/logística.
     * @return Respuesta generada por el asistente.
     */
    @Override
    public String consultarIA(String pregunta) {
        String prompt = "Eres el asistente inteligente del Banco de Alimentos de Medellín. " +
                "Responde de forma clara, técnica y concisa a la siguiente consulta sobre gestión de donaciones, logística o seguridad alimentaria:\n\n" + pregunta;
        return llamarGroq(prompt);
    }

    /**
     * Realiza la llamada HTTP POST mediante {@link RestTemplate} hacia la API de Groq Cloud
     * con autenticación Bearer y formato JSON estándar OpenAI.
     *
     * @param userPrompt Texto consolidado del requerimiento enviado al modelo.
     * @return Contenido textual devuelto por el modelo o mensaje de error descriptivo.
     */
    @SuppressWarnings("unchecked")
    private String llamarGroq(String userPrompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", modelName);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 800);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> msg = new HashMap<>();
            msg.put("role", "user");
            msg.put("content", userPrompt);
            messages.add(msg);

            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
            return "No se pudo obtener una respuesta válida del modelo de IA.";
        } catch (Exception e) {
            return "Error al conectar con el servicio de Inteligencia Artificial Groq (" + modelName + "): " + e.getMessage();
        }
    }
}
