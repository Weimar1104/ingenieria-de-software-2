package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Inventario}.
 * <p>
 * Gestiona el registro único de existencias en bodega central por cada producto (RN-07),
 * permitiendo consultas directas por clave foránea del producto.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /**
     * Busca el registro de inventario físico asociado a un producto mediante su identificador.
     *
     * @param idProducto Identificador del producto alimenticio.
     * @return {@link Optional} con el registro de inventario si existe.
     */
    Optional<Inventario> findByProducto_IdProducto(Long idProducto);

    /**
     * Método de conveniencia que redirige a {@link #findByProducto_IdProducto(Long)}.
     *
     * @param idProducto Identificador del producto alimenticio.
     * @return {@link Optional} con el registro de inventario si existe.
     */
    default Optional<Inventario> findByProductoId(Long idProducto) {
        return findByProducto_IdProducto(idProducto);
    }
}
