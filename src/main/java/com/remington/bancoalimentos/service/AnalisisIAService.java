package com.remington.bancoalimentos.service;

/**
 * Contrato de servicio para el Módulo de Inteligencia Artificial y Analítica Predictiva.
 * <p>
 * Define las operaciones de inferencia basadas en LLM (Groq Cloud / GPT OSS 120B)
 * para dar cumplimiento a las metas de optimización logística y reducción de pérdidas
 * de alimentos establecidas en la Sección 1.2 del ECS-01.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
public interface AnalisisIAService {

    /**
     * Realiza un diagnóstico global del estado del inventario físico, los históricos de donaciones
     * y los despachos a comedores para generar proyecciones de demanda y alertas tempranas de merma.
     *
     * @return Cadena de texto con el informe estructurado generado por el modelo de IA.
     */
    String generarAnalisisPredictivo();

    /**
     * Procesa una consulta personalizada en lenguaje natural sobre logística humanitaria,
     * protocolos de inocuidad o asignación equitativa en Medellín.
     *
     * @param pregunta Interrogante o solicitud formulada por el usuario.
     * @return Respuesta fundamentada emitida por el asistente inteligente.
     */
    String consultarIA(String pregunta);
}
