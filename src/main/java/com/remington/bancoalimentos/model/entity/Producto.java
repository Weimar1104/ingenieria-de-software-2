package com.remington.bancoalimentos.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entidad JPA que representa los productos o alimentos no perecederos catalogados en el banco.
 * <p>
 * Mapea la tabla {@code PRODUCTO} en 3FN conforme a los requisitos funcionales RF-13 a RF-16 e historias
 * de usuario HU-17 a HU-20. Cada producto se vincula de forma unívoca a un registro de inventario (RN-07).
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Entity
@Table(name = "PRODUCTO")
public class Producto implements Serializable {

    /**
     * Identificador primario autoincremental del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    /**
     * Nombre descriptivo y único del alimento en el catálogo (e.g. "Arroz Blanco Diana").
     */
    @Column(name = "nombre_producto", nullable = false, unique = true, length = 100)
    private String nombreProducto;

    /**
     * Categoría nutricional o de almacenamiento (e.g. "Granos y Cereales", "Aceites y Grasas").
     */
    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    /**
     * Unidad de medida física de almacenamiento y despacho (e.g. "Kg", "Litros", "Unidades").
     */
    @Column(name = "unidad_medida", nullable = false, length = 20)
    private String unidadMedida;

    /**
     * Estado operativo en el catálogo ("ACTIVO" o "INACTIVO").
     */
    @Column(name = "estado", nullable = false, length = 15)
    private String estado = "ACTIVO";

    /**
     * Constructor por defecto para JPA.
     */
    public Producto() {}

    /**
     * Constructor para inicializar productos con atributos básicos.
     *
     * @param nombreProducto Nombre único del alimento.
     * @param categoria      Categoría alimenticia.
     * @param unidadMedida   Unidad métrica de control.
     */
    public Producto(String nombreProducto, String categoria, String unidadMedida) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.unidadMedida = unidadMedida;
        this.estado = "ACTIVO";
    }

    /**
     * Obtiene el identificador del producto.
     * @return Identificador numérico.
     */
    public Long getIdProducto() { return idProducto; }

    /**
     * Asigna el identificador del producto.
     * @param idProducto Identificador numérico.
     */
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    /**
     * Obtiene el nombre del alimento.
     * @return Nombre del producto.
     */
    public String getNombreProducto() { return nombreProducto; }

    /**
     * Asigna el nombre del alimento.
     * @param nombreProducto Nombre único.
     */
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    /**
     * Obtiene la categoría del alimento.
     * @return Categoría.
     */
    public String getCategoria() { return categoria; }

    /**
     * Asigna la categoría del alimento.
     * @param categoria Categoría.
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /**
     * Obtiene la unidad de medida.
     * @return Unidad métrica.
     */
    public String getUnidadMedida() { return unidadMedida; }

    /**
     * Asigna la unidad de medida.
     * @param unidadMedida Unidad métrica.
     */
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    /**
     * Obtiene el estado del producto.
     * @return "ACTIVO" o "INACTIVO".
     */
    public String getEstado() { return estado; }

    /**
     * Asigna el estado del producto.
     * @param estado "ACTIVO" o "INACTIVO".
     */
    public void setEstado(String estado) { this.estado = estado; }
}
