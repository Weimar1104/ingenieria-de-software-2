package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa a las entidades o personas donantes de alimentos.
 * <p>
 * Mapea la tabla {@code DONANTE} conforme a los requisitos funcionales RF-09 a RF-12 e historias
 * de usuario HU-09 a HU-12, respaldando la regla de negocio RN-04 (solo donantes en estado activo
 * pueden registrar ingresos de donaciones).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "DONANTE")
public class Donante implements Serializable {

    /**
     * Identificador primario autoincremental del donante.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donante")
    private Long idDonante;

    /**
     * Razón social formal o nombre completo del donante (único en el sistema).
     */
    @Column(name = "nombre_razon_social", nullable = false, unique = true, length = 120)
    private String nombreRazonSocial;

    /**
     * Clasificación tipológica: Supermercado, Comercio, Organización, Empresa o Particular.
     */
    @Column(name = "tipo_donante", nullable = false, length = 40)
    private String tipoDonante;

    /**
     * Dirección física o sede del donante en el área metropolitana de Medellín.
     */
    @Column(name = "ubicacion", nullable = false, length = 150)
    private String ubicacion;

    /**
     * Línea telefónica de contacto logístico.
     */
    @Column(name = "telefono_contacto", nullable = false, length = 30)
    private String telefonoContacto;

    /**
     * Correo electrónico de notificación y recepción de constancias de donación.
     */
    @Column(name = "correo_contacto", length = 100)
    private String correoContacto;

    /**
     * Estado operativo ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public Donante() {}

    /**
     * Obtiene el identificador del donante.
     * @return Identificador numérico.
     */
    public Long getIdDonante() { return idDonante; }

    /**
     * Asigna el identificador del donante.
     * @param idDonante Identificador numérico.
     */
    public void setIdDonante(Long idDonante) { this.idDonante = idDonante; }

    /**
     * Obtiene la razón social o nombre comercial.
     * @return Nombre o razón social.
     */
    public String getNombreRazonSocial() { return nombreRazonSocial; }

    /**
     * Asigna la razón social o nombre comercial.
     * @param nombreRazonSocial Nombre único del donante.
     */
    public void setNombreRazonSocial(String nombreRazonSocial) { this.nombreRazonSocial = nombreRazonSocial; }

    /**
     * Obtiene el tipo de donante.
     * @return Clasificación tipológica.
     */
    public String getTipoDonante() { return tipoDonante; }

    /**
     * Asigna el tipo de donante.
     * @param tipoDonante Clasificación tipológica.
     */
    public void setTipoDonante(String tipoDonante) { this.tipoDonante = tipoDonante; }

    /**
     * Obtiene la dirección física del donante.
     * @return Ubicación física.
     */
    public String getUbicacion() { return ubicacion; }

    /**
     * Asigna la dirección física del donante.
     * @param ubicacion Ubicación física.
     */
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    /**
     * Obtiene el teléfono de contacto.
     * @return Número telefónico.
     */
    public String getTelefonoContacto() { return telefonoContacto; }

    /**
     * Asigna el teléfono de contacto.
     * @param telefonoContacto Número telefónico.
     */
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    /**
     * Obtiene el correo electrónico de contacto.
     * @return Correo electrónico.
     */
    public String getCorreoContacto() { return correoContacto; }

    /**
     * Asigna el correo electrónico de contacto.
     * @param correoContacto Correo electrónico.
     */
    public void setCorreoContacto(String correoContacto) { this.correoContacto = correoContacto; }

    /**
     * Obtiene el estado del donante.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado operativo del donante.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
