package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Donante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Donante}.
 * <p>
 * Brinda soporte a las operaciones de registro, unicidad de razón social y consulta
 * de donantes aliados (supermercados, empresas, personas naturales) según RF-09 a RF-12.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface DonanteRepository extends JpaRepository<Donante, Long> {

    /**
     * Busca un donante por su nombre o razón social exacta.
     *
     * @param nombreRazonSocial Nombre o razón social del donante.
     * @return {@link Optional} con el donante encontrado, o vacío si no existe.
     */
    Optional<Donante> findByNombreRazonSocial(String nombreRazonSocial);

    /**
     * Determina si ya existe un donante registrado con la razón social especificada.
     *
     * @param nombreRazonSocial Nombre o razón social a validar.
     * @return true si ya existe; false en caso contrario.
     */
    boolean existsByNombreRazonSocial(String nombreRazonSocial);
}
