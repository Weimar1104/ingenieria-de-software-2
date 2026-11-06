package com.remington.bancoalimentos.controller;

import com.remington.bancoalimentos.service.AnalisisIAService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web Spring MVC para el Módulo de Inteligencia Artificial y Analítica Predictiva.
 * <p>
 * Implementa las capacidades analíticas estipuladas en la Sección 1.2 del Documento de Ingeniería ECS-01:
 * </p>
 * <ul>
 *   <li>Predicción autónoma de riesgos de merma por caducidad y estimación de demanda en comedores.</li>
 *   <li>Asistente interactivo para toma de decisiones logísticas, normativas y humanitarias.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Controller
@RequestMapping("/ia")
public class IAController {

    private final AnalisisIAService analisisIAService;

    /**
     * Constructor con inyección del servicio de analítica e IA.
     *
     * @param analisisIAService Servicio de integración con el motor Groq Cloud (GPT OSS 120B).
     */
    public IAController(AnalisisIAService analisisIAService) {
        this.analisisIAService = analisisIAService;
    }

    /**
     * Despliega la vista interactiva del panel de Inteligencia Artificial.
     *
     * @param model Objeto {@link Model} para renderizado de plantillas.
     * @return El nombre lógico de la vista "ia".
     */
    @GetMapping
    public String vistaIA(Model model) {
        model.addAttribute("resultado", null);
        model.addAttribute("pregunta", "");
        return "ia";
    }

    /**
     * Atiende la solicitud POST para generar el diagnóstico predictivo integral de inventario y mermas.
     *
     * @param model Objeto {@link Model} para suministrar el diagnóstico generado por la IA a la vista.
     * @return Vista "ia" con el resultado del análisis predictivo.
     */
    @PostMapping("/analisis-predictivo")
    public String generarAnalisisPredictivo(Model model) {
        String resultado = analisisIAService.generarAnalisisPredictivo();
        model.addAttribute("resultado", resultado);
        model.addAttribute("tipo", "ANÁLISIS PREDICTIVO DE DEMANDA Y EXISTENCIAS (GROQ CLOUD)");
        return "ia";
    }

    /**
     * Atiende la consulta libre enviada al Asistente Inteligente de logística y distribución.
     *
     * @param pregunta Interrogante o solicitud formulada por el usuario en lenguaje natural.
     * @param model    Objeto {@link Model} para desplegar la respuesta procesada por el LLM.
     * @return Vista "ia" con la respuesta generada.
     */
    @PostMapping("/consultar")
    public String consultarIA(@RequestParam String pregunta, Model model) {
        if (pregunta == null || pregunta.isBlank()) {
            model.addAttribute("resultado", "Por favor ingresa una pregunta o consulta logística.");
        } else {
            String respuesta = analisisIAService.consultarIA(pregunta);
            model.addAttribute("resultado", respuesta);
            model.addAttribute("pregunta", pregunta);
            model.addAttribute("tipo", "CONSULTA ASISTENTE INTELIGENTE");
        }
        return "ia";
    }
}
