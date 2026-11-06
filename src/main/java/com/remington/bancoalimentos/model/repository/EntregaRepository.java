package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad cabecera {@link Entrega}.
 * <p>
 * Facilita operaciones de auditoría, trazabilidad de despachos y conteo de actas
 * asignadas a comedores comunitarios conforme a los requisitos RF-33 a RF-36.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    /**
     * Cuenta la cantidad de actas de entrega emitidas a favor de un beneficiario determinado.
     *
     * @param idBeneficiario Identificador del beneficiario.
     * @param estado         Estado de la entrega (e.g. "ACTIVO").
     * @return Total de entregas registradas.
     */
    long countByBeneficiario_IdBeneficiarioAndEstado(Long idBeneficiario, String estado);
}
