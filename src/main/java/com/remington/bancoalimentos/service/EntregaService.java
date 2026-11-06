package com.remington.bancoalimentos.service;

import com.remington.bancoalimentos.model.entity.Entrega;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de servicio para la gestión de despachos comunitarios y actas de entrega.
 * <p>
 * Implementa la lógica de negocio para:
 * </p>
 * <ul>
 *   <li>RN-12: Asignación exclusiva a beneficiarios en estratos socioeconómicos 1, 2 y 3.</li>
 *   <li>RN-16: Validación de existencias físicas para evitar sobregiros de inventario.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
public interface EntregaService {

    /**
     * Formaliza un despacho comunitario hacia una entidad beneficiaria legalmente registrada,
     * debitando el stock correspondiente de forma atómica.
     *
     * @param idBeneficiario Identificador de la institución o comedor receptor (RN-12).
     * @param idUsuario      Identificador del funcionario responsable del despacho.
     * @param fecha          Fecha de emisión del acta y entrega.
     * @param ruta           Identificación de la ruta o código de acta de entrega.
     * @param idProducto     Identificador del producto alimenticio a entregar.
     * @param cantidad       Cantidad a despachar.
     * @param observaciones  Anotaciones o evidencias de la entrega.
     * @return La entidad {@link Entrega} persistida con su detalle.
     * @throws BusinessRuleException Si el estrato es mayor a 3 (RN-12) o no hay stock suficiente (RN-16).
     */
    Entrega registrarEntregaConDetalle(Long idBeneficiario, Long idUsuario, LocalDate fecha, String ruta, Long idProducto, BigDecimal cantidad, String observaciones);

    /**
     * Retorna el histórico completo de despachos y entregas formalizadas en el banco.
     *
     * @return Lista de entidades {@link Entrega}.
     */
    List<Entrega> listarTodas();
}
