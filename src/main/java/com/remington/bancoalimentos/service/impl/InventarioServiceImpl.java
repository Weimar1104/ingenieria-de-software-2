package com.remington.bancoalimentos.service.impl;

import com.remington.bancoalimentos.model.entity.Inventario;
import com.remington.bancoalimentos.model.entity.Producto;
import com.remington.bancoalimentos.model.repository.InventarioRepository;
import com.remington.bancoalimentos.model.repository.ProductoRepository;
import com.remington.bancoalimentos.service.BusinessRuleException;
import com.remington.bancoalimentos.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión y consistencia del inventario físico.
 * <p>
 * Aplica transacciones atómicas y garantiza el estricto cumplimiento de:
 * </p>
 * <ul>
 *   <li><b>RN-07:</b> Unicidad de registro en inventario por cada producto registrado en el catálogo.</li>
 *   <li><b>RN-16:</b> Control de sobregiros, impidiendo que el stock disponible alcance valores negativos.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección de los repositorios de inventario y producto.
     *
     * @param inventarioRepository Repositorio de persistencia del inventario.
     * @param productoRepository   Repositorio del catálogo de productos.
     */
    public InventarioServiceImpl(InventarioRepository inventarioRepository, ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Suma unidades al stock disponible de un producto tras registrarse una donación calificada como apta.
     *
     * @param idProducto Identificador del producto a reabastecer.
     * @param cantidad   Cantidad neta a sumar (mayor a cero).
     * @return Entidad {@link Inventario} actualizada.
     * @throws BusinessRuleException Si el producto no existe o la cantidad no es positiva.
     */
    @Override
    @Transactional
    public Inventario incrementarStock(Long idProducto, BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La cantidad a incrementar debe ser mayor a cero.");
        }

        Inventario inv = inventarioRepository.findByProductoId(idProducto)
                .orElseGet(() -> {
                    Producto prod = productoRepository.findById(idProducto)
                            .orElseThrow(() -> new BusinessRuleException("Producto no encontrado para inventario: " + idProducto));
                    return new Inventario(prod, BigDecimal.ZERO);
                });

        inv.setCantidadDisponible(inv.getCantidadDisponible().add(cantidad));
        inv.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inv);
    }

    /**
     * Resta unidades al stock disponible en bodega tras un despacho comunitario,
     * impidiendo que la existencia resultante sea inferior a cero (RN-16).
     *
     * @param idProducto Identificador del producto a descargar.
     * @param cantidad   Cantidad a despachar.
     * @return Entidad {@link Inventario} actualizada.
     * @throws BusinessRuleException Si no hay suficiente stock en bodega para abastecer la cantidad solicitada (RN-16).
     */
    @Override
    @Transactional
    public Inventario descontarStock(Long idProducto, BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La cantidad a descontar debe ser mayor a cero.");
        }

        Inventario inv = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new BusinessRuleException("No existe registro de inventario para el producto: " + idProducto));

        // RN-16: Validar que no se sobregire el inventario
        if (inv.getCantidadDisponible().compareTo(cantidad) < 0) {
            throw new BusinessRuleException("RN-16: Stock insuficiente para el producto '" 
                    + inv.getProducto().getNombreProducto() 
                    + "'. Disponible: " + inv.getCantidadDisponible() 
                    + ", Solicitado: " + cantidad);
        }

        inv.setCantidadDisponible(inv.getCantidadDisponible().subtract(cantidad));
        inv.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inv);
    }

    /**
     * Obtiene el listado completo de productos y sus existencias en bodega central.
     *
     * @return Lista de inventarios disponibles.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listarTodo() {
        return inventarioRepository.findAll();
    }

    /**
     * Consulta el registro de inventario puntual para un producto dado.
     *
     * @param idProducto Identificador del producto buscado.
     * @return Registro de inventario correspondiente.
     * @throws BusinessRuleException Si el producto carece de registro en inventario.
     */
    @Override
    @Transactional(readOnly = true)
    public Inventario obtenerPorProducto(Long idProducto) {
        return inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new BusinessRuleException("No se encontró inventario para el producto: " + idProducto));
    }
}
