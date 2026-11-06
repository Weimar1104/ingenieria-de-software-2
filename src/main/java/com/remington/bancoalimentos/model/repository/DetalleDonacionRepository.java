package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.DetalleDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link DetalleDonacion}.
 * <p>
 * Gestiona la persistencia de los renglones específicos de alimentos recibidos por donación,
 * permitiendo validar restricciones de unicidad compuestas (RN-05).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface DetalleDonacionRepository extends JpaRepository<DetalleDonacion, Long> {

    /**
     * Verifica la existencia previa de un producto dentro de una misma donación para evitar duplicidades (RN-05).
     *
     * @param idDonacion Identificador de la donación padre.
     * @param idProducto Identificador del producto a verificar.
     * @return true si ya existe dicho renglón en la donación; false en caso contrario.
     */
    boolean existsByDonacion_IdDonacionAndProducto_IdProducto(Long idDonacion, Long idProducto);
}
