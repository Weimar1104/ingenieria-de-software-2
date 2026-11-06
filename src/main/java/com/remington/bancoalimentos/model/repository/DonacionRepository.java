package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad cabecera {@link Donacion}.
 * <p>
 * Facilita operaciones de auditoría, conteo y trazabilidad de ingresos según los requisitos RF-21 a RF-24.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    /**
     * Cuenta la cantidad de donaciones vinculadas a un donante específico bajo un estado determinado.
     *
     * @param idDonante Identificador del donante.
     * @param estado    Estado de la donación (e.g. "ACTIVO").
     * @return Total de donaciones coincidentes.
     */
    long countByDonante_IdDonanteAndEstado(Long idDonante, String estado);
}
