package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa las existencias físicas en tiempo real de alimentos en bodega central.
 * <p>
 * Mapea la tabla {@code INVENTARIO} según los requisitos funcionales RF-29 a RF-32 e historias
 * de usuario HU-29 a HU-32. Aplica la <b>Regla de Negocio RN-07</b> (relación uno a uno estricta
 * entre cada producto y su registro único de inventario) y garantiza que el stock no sea sobregirado (RN-16).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "INVENTARIO")
public class Inventario implements Serializable {

    /**
     * Identificador primario autoincremental del registro de inventario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    /**
     * Producto alimenticio asociado (relación 1:1 estricta conforme a RN-07).
     */
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private Producto producto;

    /**
     * Masa o volumen físico actualmente disponible para despacho en bodega (RN-16).
     */
    @Column(name = "cantidad_disponible", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadDisponible = BigDecimal.ZERO;

    /**
     * Sector o bodega de almacenamiento físico (e.g. "Bodega Central Medellín").
     */
    @Column(name = "ubicacion_bodega", nullable = false, length = 60)
    private String ubicacionBodega = "Bodega Central Medellín";

    /**
     * Marca de tiempo con la fecha y hora de la última modificación de stock.
     */
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    /**
     * Estado del registro ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA. Inicializa la fecha de actualización al momento actual.
     */
    public Inventario() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Constructor de inicialización para vincular un producto con su stock inicial.
     *
     * @param producto Producto del catálogo.
     * @param cantidad Cantidad física inicial en bodega.
     */
    public Inventario(Producto producto, BigDecimal cantidad) {
        this.producto = producto;
        this.cantidadDisponible = cantidad;
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = "ACTIVO";
    }

    /**
     * Obtiene el identificador del registro de inventario.
     * @return Identificador numérico.
     */
    public Long getIdInventario() { return idInventario; }

    /**
     * Asigna el identificador del registro de inventario.
     * @param idInventario Identificador numérico.
     */
    public void setIdInventario(Long idInventario) { this.idInventario = idInventario; }

    /**
     * Obtiene el producto asociado al inventario.
     * @return Entidad {@link Producto}.
     */
    public Producto getProducto() { return producto; }

    /**
     * Asigna el producto asociado.
     * @param producto Entidad {@link Producto}.
     */
    public void setProducto(Producto producto) { this.producto = producto; }

    /**
     * Obtiene la cantidad física disponible en bodega.
     * @return Cantidad disponible.
     */
    public BigDecimal getCantidadDisponible() { return cantidadDisponible; }

    /**
     * Asigna la cantidad física disponible.
     * @param cantidadDisponible Cantidad disponible.
     */
    public void setCantidadDisponible(BigDecimal cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    /**
     * Obtiene la ubicación física de la bodega.
     * @return Ubicación de almacenamiento.
     */
    public String getUbicacionBodega() { return ubicacionBodega; }

    /**
     * Asigna la ubicación física en bodega.
     * @param ubicacionBodega Ubicación de almacenamiento.
     */
    public void setUbicacionBodega(String ubicacionBodega) { this.ubicacionBodega = ubicacionBodega; }

    /**
     * Obtiene la marca de tiempo de la última actualización.
     * @return Fecha y hora de modificación.
     */
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    /**
     * Asigna la marca de tiempo de modificación.
     * @param fechaActualizacion Fecha y hora de modificación.
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    /**
     * Obtiene el estado del inventario.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado del inventario.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
