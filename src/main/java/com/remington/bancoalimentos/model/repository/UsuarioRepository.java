package com.remington.bancoalimentos.model.repository;

import com.remington.bancoalimentos.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad {@link Usuario}.
 * <p>
 * Facilita operaciones de autenticación de credenciales, unicidad de correo institucional
 * y verificación de usuarios vinculados a roles conforme a RF-05 a RF-08.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico única.
     *
     * @param correoElectronico Correo electrónico del usuario.
     * @return {@link Optional} con el usuario correspondiente.
     */
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    /**
     * Determina si ya existe una cuenta de usuario con el correo electrónico dado.
     *
     * @param correoElectronico Correo a verificar.
     * @return true si ya existe; false si está disponible.
     */
    boolean existsByCorreoElectronico(String correoElectronico);

    /**
     * Cuenta la cantidad de usuarios vinculados a un rol bajo un estado específico.
     *
     * @param idRol  Identificador del rol.
     * @param estado Estado del usuario (e.g. "ACTIVO").
     * @return Total de usuarios encontrados.
     */
    long countByRol_IdRolAndEstado(Long idRol, String estado);
}
