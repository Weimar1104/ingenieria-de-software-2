package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa a los funcionarios del Banco de Alimentos con acceso autenticado.
 * <p>
 * Mapea la tabla {@code USUARIO} en 3FN según los requisitos funcionales RF-05 a RF-08 e historias
 * de usuario HU-05 a HU-08, aplicando las reglas de negocio RN-01 (unicidad de correo electrónico)
 * y RN-02 (asociación obligatoria a un rol existente).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    /**
     * Identificador primario autoincremental del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    /**
     * Rol funcional asignado al usuario (RN-02).
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    /**
     * Nombre completo y apellidos del funcionario.
     */
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    /**
     * Dirección de correo electrónico institucional única (RN-01).
     */
    @Column(name = "correo_electronico", nullable = false, unique = true, length = 100)
    private String correoElectronico;

    /**
     * Resumen criptográfico de la contraseña codificada con BCrypt.
     */
    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String contrasenaHash;

    /**
     * Estado operativo de la cuenta ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public Usuario() {}

    /**
     * Obtiene el identificador del usuario.
     * @return Identificador numérico.
     */
    public Long getIdUsuario() { return idUsuario; }

    /**
     * Asigna el identificador del usuario.
     * @param idUsuario Identificador numérico.
     */
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    /**
     * Obtiene el rol asignado.
     * @return Entidad {@link Rol}.
     */
    public Rol getRol() { return rol; }

    /**
     * Asigna el rol correspondiente.
     * @param rol Entidad {@link Rol}.
     */
    public void setRol(Rol rol) { this.rol = rol; }

    /**
     * Obtiene el nombre completo del usuario.
     * @return Nombre y apellidos.
     */
    public String getNombreCompleto() { return nombreCompleto; }

    /**
     * Asigna el nombre completo del usuario.
     * @param nombreCompleto Nombre y apellidos.
     */
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    /**
     * Obtiene el correo electrónico único.
     * @return Dirección de correo.
     */
    public String getCorreoElectronico() { return correoElectronico; }

    /**
     * Asigna el correo electrónico.
     * @param correoElectronico Dirección de correo.
     */
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    /**
     * Obtiene el hash BCrypt de la contraseña.
     * @return Hash de contraseña.
     */
    public String getContrasenaHash() { return contrasenaHash; }

    /**
     * Asigna el hash de la contraseña.
     * @param contrasenaHash Hash BCrypt generado.
     */
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    /**
     * Obtiene el estado de la cuenta.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado de la cuenta.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
