package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa a las poblaciones, comedores comunitarios o familias beneficiarias.
 * <p>
 * Mapea la tabla {@code BENEFICIARIO} según los requisitos funcionales RF-17 a RF-20 e historias
 * de usuario HU-13 a HU-16. Aplica estrictamente la <b>Regla de Negocio RN-12</b>, que estipula
 * que los beneficiarios atendidos deben pertenecer exclusivamente a los estratos socioeconómicos 1, 2 y 3.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "BENEFICIARIO")
public class Beneficiario implements Serializable {

    /**
     * Identificador primario autoincremental del beneficiario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_beneficiario")
    private Long idBeneficiario;

    /**
     * Nombre formal de la fundación, comedor comunitario o representante familiar (único en el sistema).
     */
    @Column(name = "nombre_beneficiario", nullable = false, unique = true, length = 120)
    private String nombreBeneficiario;

    /**
     * Tipología del beneficiario: Fundación, Comedor Comunitario o Familia Vulnerable.
     */
    @Column(name = "tipo_beneficiario", nullable = false, length = 40)
    private String tipoBeneficiario;

    /**
     * Estrato socioeconómico validado (RN-12: Exclusivo estratos 1, 2 y 3).
     */
    @Column(name = "estrato_socioeconomico", nullable = false)
    private Integer estratoSocioeconomico;

    /**
     * Comuna o barrio de ubicación en Medellín (e.g. "Comuna 13 - San Javier", "Comuna 1 - Popular").
     */
    @Column(name = "ubicacion_comuna", nullable = false, length = 150)
    private String ubicacionComuna;

    /**
     * Línea telefónica de contacto del encargado del comedor o beneficiario.
     */
    @Column(name = "telefono_contacto", nullable = false, length = 30)
    private String telefonoContacto;

    /**
     * Modalidad o tipo de ración alimentaria solicitada.
     */
    @Column(name = "tipo_ayuda_solicitada", nullable = false, length = 100)
    private String tipoAyudaSolicitada;

    /**
     * Estado operativo ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public Beneficiario() {}

    /**
     * Obtiene el identificador del beneficiario.
     * @return Identificador numérico.
     */
    public Long getIdBeneficiario() { return idBeneficiario; }

    /**
     * Asigna el identificador del beneficiario.
     * @param idBeneficiario Identificador numérico.
     */
    public void setIdBeneficiario(Long idBeneficiario) { this.idBeneficiario = idBeneficiario; }

    /**
     * Obtiene el nombre del beneficiario o institución.
     * @return Nombre del beneficiario.
     */
    public String getNombreBeneficiario() { return nombreBeneficiario; }

    /**
     * Asigna el nombre del beneficiario.
     * @param nombreBeneficiario Nombre único.
     */
    public void setNombreBeneficiario(String nombreBeneficiario) { this.nombreBeneficiario = nombreBeneficiario; }

    /**
     * Obtiene la clasificación tipológica del beneficiario.
     * @return Tipo de beneficiario.
     */
    public String getTipoBeneficiario() { return tipoBeneficiario; }

    /**
     * Asigna la clasificación tipológica del beneficiario.
     * @param tipoBeneficiario Tipo de beneficiario.
     */
    public void setTipoBeneficiario(String tipoBeneficiario) { this.tipoBeneficiario = tipoBeneficiario; }

    /**
     * Obtiene el estrato socioeconómico (1, 2 o 3 según RN-12).
     * @return Número de estrato socioeconómico.
     */
    public Integer getEstratoSocioeconomico() { return estratoSocioeconomico; }

    /**
     * Asigna el estrato socioeconómico.
     * @param estratoSocioeconomico Número de estrato (RN-12).
     */
    public void setEstratoSocioeconomico(Integer estratoSocioeconomico) { this.estratoSocioeconomico = estratoSocioeconomico; }

    /**
     * Obtiene la comuna o barrio en Medellín.
     * @return Comuna de ubicación.
     */
    public String getUbicacionComuna() { return ubicacionComuna; }

    /**
     * Asigna la comuna o sector de Medellín.
     * @param ubicacionComuna Comuna de ubicación.
     */
    public void setUbicacionComuna(String ubicacionComuna) { this.ubicacionComuna = ubicacionComuna; }

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
     * Obtiene el tipo de ración alimentaria solicitada.
     * @return Tipo de ayuda solicitada.
     */
    public String getTipoAyudaSolicitada() { return tipoAyudaSolicitada; }

    /**
     * Asigna el tipo de ayuda solicitada.
     * @param tipoAyudaSolicitada Tipo de ayuda.
     */
    public void setTipoAyudaSolicitada(String tipoAyudaSolicitada) { this.tipoAyudaSolicitada = tipoAyudaSolicitada; }

    /**
     * Obtiene el estado operativo del beneficiario.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado operativo del beneficiario.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
