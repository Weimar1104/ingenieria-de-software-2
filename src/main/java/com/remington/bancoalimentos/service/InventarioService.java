package com.remington.bancoalimentos.service;

import com.remington.bancoalimentos.model.entity.Inventario;
import java.math.BigDecimal;
import java.util.List;

/**
 * Contrato de servicio para el control de inventario y existencias físicas en bodega central.
 * <p>
 * Aplica el principio de responsabilidad única (SRP) garantizando:
 * </p>
 * <ul>
 *   <li>RN-07: Un único registro consolidado de inventario por cada producto del catálogo.</li>
 *   <li>RN-16: Consistencia transaccional e impedimento de inventarios negativos (sobregiros).</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
public interface InventarioService {

    /**
     * Incrementa las existencias disponibles de un alimento en bodega tras una donación apta.
     *
     * @param idProducto Identificador del producto alimenticio.
     * @param cantidad   Cantidad a incrementar (mayor a cero).
     * @return Registro de inventario actualizado.
     */
    Inventario incrementarStock(Long idProducto, BigDecimal cantidad);

    /**
     * Decrementa las existencias disponibles en bodega tras una entrega comunitaria,
     * validando previamente que la cantidad disponible no sea sobregirada.
     *
     * @param idProducto Identificador del producto a descontar.
     * @param cantidad   Cantidad a descontar.
     * @return Registro de inventario actualizado.
     * @throws BusinessRuleException Si el stock actual es insuficiente para cubrir la entrega (RN-16).
     */
    Inventario descontarStock(Long idProducto, BigDecimal cantidad);

    /**
     * Consulta el catálogo completo de existencias en bodega central.
     *
     * @return Lista de registros de {@link Inventario}.
     */
    List<Inventario> listarTodo();

    /**
     * Obtiene el registro de inventario asociado a un producto en particular.
     *
     * @param idProducto Identificador del producto a consultar.
     * @return Registro de inventario encontrado.
     * @throws BusinessRuleException Si el producto no tiene registro de bodega activo.
     */
    Inventario obtenerPorProducto(Long idProducto);

    /**
     * Consulta directamente la cantidad numérica disponible en bodega para un producto dado.
     *
     * @param idProducto Identificador del producto a consultar.
     * @return Cantidad física actualmente en existencia (stock).
     */
    default BigDecimal consultarStock(Long idProducto) {
        return obtenerPorProducto(idProducto).getCantidadDisponible();
    }
}
