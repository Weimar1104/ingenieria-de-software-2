package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.DetalleEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link DetalleEntrega}.
 * <p>
 * Gestiona los renglones individuales de productos asignados en cada acta de entrega
 * comunitaria, permitiendo validar restricciones de unicidad compuesta (RN-10).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface DetalleEntregaRepository extends JpaRepository<DetalleEntrega, Long> {

    /**
     * Verifica si un producto ya se encuentra registrado dentro de una misma entrega (RN-10).
     *
     * @param idEntrega  Identificador del acta de entrega.
     * @param idProducto Identificador del producto a verificar.
     * @return true si ya existe el producto en el acta; false en caso contrario.
     */
    boolean existsByEntrega_IdEntregaAndProducto_IdProducto(Long idEntrega, Long idProducto);
}
