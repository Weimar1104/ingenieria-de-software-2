package com.remington.bancoalimentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque del Sistema de Gestión para el Banco de Alimentos Inteligente de Medellín.
 * <p>
 * Este sistema representa el Elemento de Configuración del Software (ECS-01) desarrollado para la asignatura
 * de Ingeniería de Software II en la Corporación Universitaria Remington.
 * </p>
 * <p>
 * Integra la arquitectura MVC, persistencia relacional en 3FN con Spring Data JPA / Hibernate,
 * autenticación y autorización por roles mediante Spring Security, motor de plantillas Thymeleaf
 * y el Módulo de Analítica e Inteligencia Artificial predictiva con Groq Cloud (modelo GPT OSS 120B).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0 (Línea Base LB-2 / 2026)
 */
@SpringBootApplication
public class BancoAlimentosApplication {

    /**
     * Punto de entrada principal para el inicio de la aplicación en el servidor embebido Tomcat.
     *
     * @param args Argumentos de la línea de comandos pasados durante la invocación.
     */
    public static void main(String[] args) {
        SpringApplication.run(BancoAlimentosApplication.class, args);
    }
}
