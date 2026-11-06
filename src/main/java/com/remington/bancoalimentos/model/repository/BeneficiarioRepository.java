package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Beneficiario}.
 * <p>
 * Proporciona métodos CRUD y consultas derivadas de Spring Data JPA para la administración
 * de comedores comunitarios y familias beneficiarias conforme a los requisitos RF-17 a RF-20.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {

    /**
     * Consulta todos los beneficiarios que coincidan con el estado especificado (e.g. "ACTIVO").
     *
     * @param estado Estado de habilitación en el sistema.
     * @return Lista de beneficiarios con dicho estado.
     */
    List<Beneficiario> findByEstado(String estado);
}
