package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa la cabecera del acta de despacho y entrega comunitaria de alimentos.
 * <p>
 * Mapea la tabla {@code ENTREGA} conforme a los requisitos funcionales RF-33 a RF-36 e historias
 * de usuario HU-33 a HU-36. Asocia al beneficiario calificado en estratos 1 a 3 (RN-12), al funcionario
 * responsable del despacho y agrupa los renglones de alimentos despachados ({@link DetalleEntrega}).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "ENTREGA")
public class Entrega implements Serializable {

    /**
     * Identificador primario autoincremental de la entrega.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Long idEntrega;

    /**
     * Beneficiario receptor de la ayuda (restringido a estratos 1, 2 y 3 según RN-12).
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_beneficiario", nullable = false)
    private Beneficiario beneficiario;

    /**
     * Funcionario del banco responsable de autorizar y legalizar el despacho.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_usuario_responsable", nullable = false)
    private Usuario usuarioResponsable;

    /**
     * Fecha formal de la entrega o expedición del acta.
     */
    @Column(name = "fecha_entrega", nullable = false)
    private LocalDate fechaEntrega;

    /**
     * Código formal del acta de entrega o identificación de la ruta de distribución.
     */
    @Column(name = "ruta_entrega", nullable = false, length = 150)
    private String rutaEntrega;

    /**
     * Observaciones, firmas o constancias de entrega de la ración comunitaria.
     */
    @Column(name = "observaciones_evidencia", length = 250)
    private String observacionesEvidencia;

    /**
     * Estado operativo de la entrega ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Colección de productos individuales despachados en esta entrega.
     */
    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleEntrega> detalles = new ArrayList<>();

    /**
     * Constructor por defecto para JPA.
     */
    public Entrega() {}

    /**
     * Obtiene el identificador de la entrega.
     * @return Identificador numérico.
     */
    public Long getIdEntrega() { return idEntrega; }

    /**
     * Asigna el identificador de la entrega.
     * @param idEntrega Identificador numérico.
     */
    public void setIdEntrega(Long idEntrega) { this.idEntrega = idEntrega; }

    /**
     * Obtiene el beneficiario de la entrega.
     * @return Entidad {@link Beneficiario}.
     */
    public Beneficiario getBeneficiario() { return beneficiario; }

    /**
     * Asigna el beneficiario de la entrega.
     * @param beneficiario Entidad {@link Beneficiario}.
     */
    public void setBeneficiario(Beneficiario beneficiario) { this.beneficiario = beneficiario; }

    /**
     * Obtiene el usuario responsable del despacho.
     * @return Entidad {@link Usuario}.
     */
    public Usuario getUsuarioResponsable() { return usuarioResponsable; }

    /**
     * Asigna el usuario responsable del despacho.
     * @param usuarioResponsable Entidad {@link Usuario}.
     */
    public void setUsuarioResponsable(Usuario usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }

    /**
     * Obtiene la fecha de entrega.
     * @return Fecha del despacho.
     */
    public LocalDate getFechaEntrega() { return fechaEntrega; }

    /**
     * Asigna la fecha de entrega.
     * @param fechaEntrega Fecha del despacho.
     */
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    /**
     * Obtiene el código de acta o ruta de entrega.
     * @return Ruta o acta.
     */
    public String getRutaEntrega() { return rutaEntrega; }

    /**
     * Asigna el código de acta o ruta de entrega.
     * @param rutaEntrega Ruta o acta.
     */
    public void setRutaEntrega(String rutaEntrega) { this.rutaEntrega = rutaEntrega; }

    /**
     * Obtiene las observaciones de evidencia.
     * @return Observaciones.
     */
    public String getObservacionesEvidencia() { return observacionesEvidencia; }

    /**
     * Asigna las observaciones de evidencia.
     * @param observacionesEvidencia Observaciones.
     */
    public void setObservacionesEvidencia(String observacionesEvidencia) { this.observacionesEvidencia = observacionesEvidencia; }

    /**
     * Obtiene el estado de la entrega.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado de la entrega.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene los renglones de productos entregados.
     * @return Lista de {@link DetalleEntrega}.
     */
    public List<DetalleEntrega> getDetalles() { return detalles; }

    /**
     * Asigna los renglones de productos entregados.
     * @param detalles Lista de {@link DetalleEntrega}.
     */
    public void setDetalles(List<DetalleEntrega> detalles) { this.detalles = detalles; }

    /**
     * Método auxiliar de conveniencia bidireccional para añadir un producto al acta de entrega.
     *
     * @param detalle Renglón de alimento a vincular al despacho.
     */
    public void addDetalle(DetalleEntrega detalle) {
        detalles.add(detalle);
        detalle.setEntrega(this);
    }
}
