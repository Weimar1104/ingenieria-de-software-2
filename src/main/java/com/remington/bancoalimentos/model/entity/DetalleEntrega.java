package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad JPA que representa un renglón individual de alimento dentro de un acta de entrega comunitaria.
 * <p>
 * Mapea la tabla {@code DETALLE_ENTREGA} en 3FN según los requisitos funcionales RF-37 a RF-40
 * e historias de usuario HU-37 a HU-40. Aplica:
 * </p>
 * <ul>
 *   <li>RN-10: Restricción de unicidad compuesta entre entrega y producto para impedir duplicación de líneas.</li>
 *   <li>RN-15: Cantidades despachadas estrictamente superiores a cero.</li>
 *   <li>RN-16: Vinculación con la deducción física del inventario disponible en bodega.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "DETALLE_ENTREGA", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_entrega", "id_producto"})
})
public class DetalleEntrega implements Serializable {

    /**
     * Identificador primario autoincremental del renglón de entrega.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_entrega")
    private Long idDetalleEntrega;

    /**
     * Entrega cabecera a la que pertenece este renglón.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_entrega", nullable = false)
    private Entrega entrega;

    /**
     * Alimento específico despachado al beneficiario.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Masa o volumen físico despachado (RN-15 / RN-16).
     */
    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    /**
     * Estado operativo del registro ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public DetalleEntrega() {}

    /**
     * Obtiene el identificador del detalle de entrega.
     * @return Identificador numérico.
     */
    public Long getIdDetalleEntrega() { return idDetalleEntrega; }

    /**
     * Asigna el identificador del detalle de entrega.
     * @param idDetalleEntrega Identificador numérico.
     */
    public void setIdDetalleEntrega(Long idDetalleEntrega) { this.idDetalleEntrega = idDetalleEntrega; }

    /**
     * Obtiene el acta de entrega vinculada.
     * @return Entidad {@link Entrega}.
     */
    public Entrega getEntrega() { return entrega; }

    /**
     * Asigna el acta de entrega vinculada.
     * @param entrega Entidad {@link Entrega}.
     */
    public void setEntrega(Entrega entrega) { this.entrega = entrega; }

    /**
     * Obtiene el producto alimenticio despachado.
     * @return Entidad {@link Producto}.
     */
    public Producto getProducto() { return producto; }

    /**
     * Asigna el producto alimenticio despachado.
     * @param producto Entidad {@link Producto}.
     */
    public void setProducto(Producto producto) { this.producto = producto; }

    /**
     * Obtiene la cantidad física despachada.
     * @return Cantidad despachada.
     */
    public BigDecimal getCantidad() { return cantidad; }

    /**
     * Asigna la cantidad física despachada.
     * @param cantidad Cantidad despachada.
     */
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

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
