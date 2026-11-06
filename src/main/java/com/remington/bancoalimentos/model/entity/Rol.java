package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa los roles de seguridad y acceso de los usuarios del sistema.
 * <p>
 * Mapea la tabla {@code ROL} en base de datos bajo los requisitos funcionales RF-01 a RF-04
 * e historias de usuario HU-01 a HU-04, haciendo cumplir la regla de negocio RN-03 (unicidad de nombres de rol).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "ROL")
public class Rol implements Serializable {

    /**
     * Identificador primario autoincremental del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    /**
     * Nombre descriptivo y único del rol institucional (e.g. "Administrador del Sistema").
     */
    @Column(name = "nombre_rol", nullable = false, unique = true, length = 50)
    private String nombreRol;

    /**
     * Descripción detallada del alcance funcional del rol.
     */
    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    /**
     * Estado lógico de habilitación ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto requerido por el framework JPA / Hibernate.
     */
    public Rol() {}

    /**
     * Constructor de conveniencia para inicializar roles con nombre y descripción.
     *
     * @param nombreRol   Nombre único del rol.
     * @param descripcion Descripción funcional del rol.
     */
    public Rol(String nombreRol, String descripcion) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.estado = "ACTIVO";
    }

    /**
     * Obtiene el identificador del rol.
     * @return Identificador numérico único.
     */
    public Long getIdRol() { return idRol; }

    /**
     * Asigna el identificador del rol.
     * @param idRol Identificador numérico único.
     */
    public void setIdRol(Long idRol) { this.idRol = idRol; }

    /**
     * Obtiene el nombre del rol.
     * @return Nombre del rol.
     */
    public String getNombreRol() { return nombreRol; }

    /**
     * Asigna el nombre del rol.
     * @param nombreRol Nombre único del rol.
     */
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    /**
     * Obtiene la descripción del rol.
     * @return Texto descriptivo.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Asigna la descripción del rol.
     * @param descripcion Texto descriptivo.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * Obtiene el estado operativo del rol.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado operativo del rol.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
