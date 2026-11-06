package com.remington.bancoalimentos.service;

import com.remington.bancoalimentos.model.entity.Donacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de servicio para la gestión de donaciones e inspección de inocuidad alimentaria.
 * <p>
 * Aplica el principio de responsabilidad única (SRP) para encapsular las reglas:
 * </p>
 * <ul>
 *   <li>RN-04: Exclusividad de donantes activos.</li>
 *   <li>RN-11: Restricción de inactivación sobre donaciones con detalles asociados.</li>
 *   <li>RN-14: Evaluación de calidad sanitaria de los alimentos recibidos.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
public interface DonacionService {

    /**
     * Registra de forma transaccional una nueva donación con su respectivo detalle de producto,
     * evaluando su calidad sanitaria e incrementando el inventario disponible si resulta apto.
     *
     * @param idDonante       Identificador del donante remitente.
     * @param idUsuario       Identificador del usuario receptor.
     * @param fecha           Fecha de recepción de la donación.
     * @param idProducto      Identificador del producto donado.
     * @param cantidad        Masa o volumen recibido (mayor a cero).
     * @param estadoAlimento  Resultado de la inspección sanitaria (APTO o RECHAZADO).
     * @param observaciones   Anotaciones sobre el lote, embalaje o transporte.
     * @return La entidad {@link Donacion} persistida.
     * @throws BusinessRuleException Si el donante está inactivo (RN-04) o el alimento es rechazado (RN-14).
     */
    Donacion registrarDonacionConDetalle(Long idDonante, Long idUsuario, LocalDate fecha, Long idProducto, BigDecimal cantidad, String estadoAlimento, String observaciones);

    /**
     * Retorna el catálogo histórico de todas las donaciones registradas en el sistema.
     *
     * @return Lista inmutable o mutable de entidades {@link Donacion}.
     */
    List<Donacion> listarTodas();

    /**
     * Modifica el estado de una donación a inactivo conforme a la regla RN-11.
     *
     * @param idDonacion Identificador de la donación a inactivar.
     * @throws BusinessRuleException Si la donación contiene alimentos asociados (RN-11).
     */
    void inactivarDonacion(Long idDonacion);
}
