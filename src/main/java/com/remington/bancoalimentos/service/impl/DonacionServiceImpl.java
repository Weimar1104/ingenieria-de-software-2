package com.remington.bancoalimentos.service.impl;

import com.remington.bancoalimentos.model.entity.*;
import com.remington.bancoalimentos.model.repository.*;
import com.remington.bancoalimentos.service.BusinessRuleException;
import com.remington.bancoalimentos.service.DonacionService;
import com.remington.bancoalimentos.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la recepción y trazabilidad de donaciones.
 * <p>
 * Aplica transaccionalidad atómica y hace cumplir las siguientes reglas de negocio del ECS-01:
 * </p>
 * <ul>
 *   <li><b>RN-04:</b> Solamente los donantes con estado "ACTIVO" están habilitados para realizar donaciones.</li>
 *   <li><b>RN-11:</b> No es posible inactivar una donación que posea detalles de alimentos vinculados.</li>
 *   <li><b>RN-14:</b> Los productos en estado "RECHAZADO" por averías o no conformidad sanitaria son bloqueados para ingreso a inventario.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Service
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;
    private final DonanteRepository donanteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final DetalleDonacionRepository detalleDonacionRepository;
    private final InventarioService inventarioService;

    /**
     * Constructor con inyección de dependencias para los repositorios de entidades y el servicio de inventario.
     *
     * @param donacionRepository        Repositorio de donaciones.
     * @param donanteRepository         Repositorio de donantes.
     * @param usuarioRepository         Repositorio de usuarios.
     * @param productoRepository        Repositorio de productos.
     * @param detalleDonacionRepository Repositorio de renglones de donación.
     * @param inventarioService         Servicio para actualización del stock central.
     */
    public DonacionServiceImpl(DonacionRepository donacionRepository, DonanteRepository donanteRepository,
                               UsuarioRepository usuarioRepository, ProductoRepository productoRepository,
                               DetalleDonacionRepository detalleDonacionRepository, InventarioService inventarioService) {
        this.donacionRepository = donacionRepository;
        this.donanteRepository = donanteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.detalleDonacionRepository = detalleDonacionRepository;
        this.inventarioService = inventarioService;
    }

    /**
     * Registra una donación, su renglón de detalle y actualiza el inventario en una única transacción atómica.
     *
     * @param idDonante       Identificador del donante.
     * @param idUsuario       Identificador del usuario receptor.
     * @param fecha           Fecha de recepción de los alimentos.
     * @param idProducto      Identificador del alimento donado.
     * @param cantidad        Masa o volumen físico recibido.
     * @param estadoAlimento  Calificación sanitaria ("APTO" o "RECHAZADO").
     * @param observaciones   Observaciones sobre el lote o estado del empaque.
     * @return Entidad {@link Donacion} creada y persistida con éxito.
     * @throws BusinessRuleException Si el donante no existe, no está activo (RN-04) o el alimento es rechazado (RN-14).
     */
    @Override
    @Transactional
    public Donacion registrarDonacionConDetalle(Long idDonante, Long idUsuario, LocalDate fecha,
                                               Long idProducto, BigDecimal cantidad, String estadoAlimento, String observaciones) {
        // RN-04: Donante debe existir y estar activo
        Donante donante = donanteRepository.findById(idDonante)
                .orElseThrow(() -> new BusinessRuleException("Donante no encontrado: " + idDonante));
        if (!"ACTIVO".equalsIgnoreCase(donante.getEstado())) {
            throw new BusinessRuleException("RN-04: No se permite registrar donaciones de donantes inactivos.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BusinessRuleException("Usuario receptor no encontrado: " + idUsuario));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new BusinessRuleException("Producto no encontrado: " + idProducto));

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La cantidad donada debe ser mayor a cero.");
        }

        // RN-14: Evaluación de calidad del alimento
        if ("RECHAZADO".equalsIgnoreCase(estadoAlimento)) {
            throw new BusinessRuleException("RN-14: El alimento se encuentra averiado o no cumple condiciones para su recepcion.");
        }

        Donacion donacion = new Donacion();
        donacion.setDonante(donante);
        donacion.setUsuarioRegistra(usuario);
        donacion.setFechaDonacion(fecha != null ? fecha : LocalDate.now());
        donacion.setObservaciones(observaciones);
        donacion.setEstado("ACTIVO");

        DetalleDonacion detalle = new DetalleDonacion();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setEstadoAlimento("APTO");
        detalle.setEstado("ACTIVO");

        donacion.addDetalle(detalle);
        Donacion saved = donacionRepository.save(donacion);

        // Actualizar Inventario (Suma de Stock)
        inventarioService.incrementarStock(idProducto, cantidad);

        return saved;
    }

    /**
     * Consulta el histórico de todas las donaciones registradas en base de datos.
     *
     * @return Lista de donaciones.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Donacion> listarTodas() {
        return donacionRepository.findAll();
    }

    /**
     * Inactiva lógicamente una donación siempre que no cuente con detalles asociados.
     *
     * @param idDonacion Identificador de la donación a dar de baja.
     * @throws BusinessRuleException Si la donación contiene alimentos asociados (RN-11).
     */
    @Override
    @Transactional
    public void inactivarDonacion(Long idDonacion) {
        Donacion d = donacionRepository.findById(idDonacion)
                .orElseThrow(() -> new BusinessRuleException("Donacion no encontrada: " + idDonacion));
        if (!d.getDetalles().isEmpty()) {
            throw new BusinessRuleException("RN-11: No se permite inactivar una donación que contenga detalles de alimentos.");
        }
        d.setEstado("INACTIVO");
        donacionRepository.save(d);
    }
}
