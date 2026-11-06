package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Rol}.
 * <p>
 * Brinda soporte al esquema de autorización por roles (RBAC) y garantiza la unicidad
 * de los nombres de rol en el sistema conforme a RF-01 a RF-04.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su nombre descriptivo único.
     *
     * @param nombreRol Nombre del rol.
     * @return {@link Optional} con el rol si existe.
     */
    Optional<Rol> findByNombreRol(String nombreRol);

    /**
     * Valida si un rol con el nombre indicado ya existe en el sistema.
     *
     * @param nombreRol Nombre del rol.
     * @return true si ya existe; false si está disponible.
     */
    boolean existsByNombreRol(String nombreRol);
}
