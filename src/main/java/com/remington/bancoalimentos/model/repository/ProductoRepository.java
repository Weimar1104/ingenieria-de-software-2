package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Producto}.
 * <p>
 * Gestiona el catálogo unificado de alimentos no perecederos clasificados por categoría
 * y unidad de medida conforme a los requisitos funcionales RF-13 a RF-16.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca un producto por su nombre oficial en el catálogo.
     *
     * @param nombreProducto Nombre del alimento.
     * @return {@link Optional} con el producto encontrado.
     */
    Optional<Producto> findByNombreProducto(String nombreProducto);

    /**
     * Determina si ya existe un alimento registrado con el nombre proporcionado.
     *
     * @param nombreProducto Nombre a comprobar.
     * @return true si ya existe; false en caso contrario.
     */
    boolean existsByNombreProducto(String nombreProducto);
}
