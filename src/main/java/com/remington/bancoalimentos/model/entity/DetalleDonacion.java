package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa un renglón específico de alimento dentro de una donación.
 * <p>
 * Mapea la tabla {@code DETALLE_DONACION} en 3FN conforme a los requisitos funcionales RF-25 a RF-28
 * e historias de usuario HU-25 a HU-28. Aplica:
 * </p>
 * <ul>
 *   <li>RN-05: Restricción de unicidad compuesta entre donación y producto para evitar duplicidad de líneas.</li>
 *   <li>RN-06: Cantidades estrictamente positivas.</li>
 *   <li>RN-14: Evaluación de calidad sanitaria individual ("APTO" vs "RECHAZADO").</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "DETALLE_DONACION", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_donacion", "id_producto"})
})
public class DetalleDonacion implements Serializable {

    /**
     * Identificador primario autoincremental del renglón de detalle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_donacion")
    private Long idDetalleDonacion;

    /**
     * Donación cabecera a la cual pertenece este detalle.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_donacion", nullable = false)
    private Donacion donacion;

    /**
     * Alimento específico recibido.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Cantidad física recibida (mayor a cero según RN-06).
     */
    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    /**
     * Calificación fitosanitaria del alimento: "APTO" o "RECHAZADO" (RN-14).
     */
    @Column(name = "estado_alimento", nullable = false, length = 30)
    private String estadoAlimento = "APTO";

    /**
     * Estado operativo del registro ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public DetalleDonacion() {}

    /**
     * Obtiene el identificador del detalle de donación.
     * @return Identificador numérico.
     */
    public Long getIdDetalleDonacion() { return idDetalleDonacion; }

    /**
     * Asigna el identificador del detalle de donación.
     * @param idDetalleDonacion Identificador numérico.
     */
    public void setIdDetalleDonacion(Long idDetalleDonacion) { this.idDetalleDonacion = idDetalleDonacion; }

    /**
     * Obtiene la donación vinculada.
     * @return Entidad {@link Donacion}.
     */
    public Donacion getDonacion() { return donacion; }

    /**
     * Asigna la donación vinculada.
     * @param donacion Entidad {@link Donacion}.
     */
    public void setDonacion(Donacion donacion) { this.donacion = donacion; }

    /**
     * Obtiene el producto alimenticio del renglón.
     * @return Entidad {@link Producto}.
     */
    public Producto getProducto() { return producto; }

    /**
     * Asigna el producto alimenticio del renglón.
     * @param producto Entidad {@link Producto}.
     */
    public void setProducto(Producto producto) { this.producto = producto; }

    /**
     * Obtiene la cantidad física donada.
     * @return Cantidad.
     */
    public BigDecimal getCantidad() { return cantidad; }

    /**
     * Asigna la cantidad física donada.
     * @param cantidad Cantidad positiva.
     */
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene el estado sanitario del alimento.
     * @return "APTO" o "RECHAZADO" (RN-14).
     */
    public String getEstadoAlimento() { return estadoAlimento; }

    /**
     * Asigna el estado sanitario del alimento.
     * @param estadoAlimento "APTO" o "RECHAZADO".
     */
    public void setEstadoAlimento(String estadoAlimento) { this.estadoAlimento = estadoAlimento; }

    /**
     * Obtiene el estado operativo del renglón.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado operativo del renglón.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
