package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa la cabecera del registro formal de una donación de alimentos.
 * <p>
 * Mapea la tabla {@code DONACION} según los requisitos funcionales RF-21 a RF-24 e historias
 * de usuario HU-21 a HU-24. Consolida la procedencia del alimento (donante activo según RN-04),
 * el funcionario responsable de la recepción y la relación uno a muchos con los renglones
 * inspeccionados ({@link DetalleDonacion}).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "DONACION")
public class Donacion implements Serializable {

    /**
     * Identificador primario autoincremental de la donación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donacion")
    private Long idDonacion;

    /**
     * Donante que suministra los alimentos (debe estar en estado ACTIVO según RN-04).
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_donante", nullable = false)
    private Donante donante;

    /**
     * Funcionario del banco de alimentos que legaliza y certifica el ingreso.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_usuario_registra", nullable = false)
    private Usuario usuarioRegistra;

    /**
     * Fecha de entrega formal de la donación.
     */
    @Column(name = "fecha_donacion", nullable = false)
    private LocalDate fechaDonacion;

    /**
     * Notas u observaciones sobre el lote, temperatura o transporte.
     */
    @Column(name = "observaciones", length = 250)
    private String observaciones;

    /**
     * Estado operativo de la donación ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Lista de renglones o productos individuales incluidos en esta donación.
     */
    @OneToMany(mappedBy = "donacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleDonacion> detalles = new ArrayList<>();

    /**
     * Constructor por defecto para JPA.
     */
    public Donacion() {}

    /**
     * Obtiene el identificador de la donación.
     * @return Identificador numérico.
     */
    public Long getIdDonacion() { return idDonacion; }

    /**
     * Asigna el identificador de la donación.
     * @param idDonacion Identificador numérico.
     */
    public void setIdDonacion(Long idDonacion) { this.idDonacion = idDonacion; }

    /**
     * Obtiene el donante asociado.
     * @return Entidad {@link Donante}.
     */
    public Donante getDonante() { return donante; }

    /**
     * Asigna el donante de la donación.
     * @param donante Entidad {@link Donante}.
     */
    public void setDonante(Donante donante) { this.donante = donante; }

    /**
     * Obtiene el usuario que registró la donación.
     * @return Entidad {@link Usuario}.
     */
    public Usuario getUsuarioRegistra() { return usuarioRegistra; }

    /**
     * Asigna el usuario responsable del registro.
     * @param usuarioRegistra Entidad {@link Usuario}.
     */
    public void setUsuarioRegistra(Usuario usuarioRegistra) { this.usuarioRegistra = usuarioRegistra; }

    /**
     * Obtiene la fecha de recepción de la donación.
     * @return Fecha de registro.
     */
    public LocalDate getFechaDonacion() { return fechaDonacion; }

    /**
     * Asigna la fecha de recepción.
     * @param fechaDonacion Fecha de la donación.
     */
    public void setFechaDonacion(LocalDate fechaDonacion) { this.fechaDonacion = fechaDonacion; }

    /**
     * Obtiene las observaciones de la donación.
     * @return Texto de observaciones.
     */
    public String getObservaciones() { return observaciones; }

    /**
     * Asigna las observaciones de la donación.
     * @param observaciones Texto de observaciones.
     */
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    /**
     * Obtiene el estado de la donación.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado operativo.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene la colección de detalles de alimentos.
     * @return Lista de renglones {@link DetalleDonacion}.
     */
    public List<DetalleDonacion> getDetalles() { return detalles; }

    /**
     * Asigna la colección de detalles.
     * @param detalles Lista de renglones.
     */
    public void setDetalles(List<DetalleDonacion> detalles) { this.detalles = detalles; }

    /**
     * Método auxiliar de conveniencia bidireccional para añadir un renglón a la donación.
     *
     * @param detalle Renglón de alimento a vincular.
     */
    public void addDetalle(DetalleDonacion detalle) {
        detalles.add(detalle);
        detalle.setDonacion(this);
    }
}
