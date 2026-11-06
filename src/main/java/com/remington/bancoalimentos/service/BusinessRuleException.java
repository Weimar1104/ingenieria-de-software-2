package com.remington.bancoalimentos.service;

/**
 * Excepción personalizada de dominio utilizada para señalar la violación de alguna de las 16 Reglas de Negocio (RN)
 * formalmente especificadas en el Documento de Ingeniería del Software (ECS-01).
 * <p>
 * Al extender de {@link RuntimeException}, permite abortar de forma no comprobada las transacciones activas
 * provocando un rollback automático en la base de datos para mantener la consistencia e integridad referencial.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Construye una nueva excepción de regla de negocio con el mensaje explicativo correspondiente.
     *
     * @param message Descripción detallada del motivo del rechazo y la regla infringida (e.g., RN-04, RN-12, RN-16).
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
