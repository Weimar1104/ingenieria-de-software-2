package com.remington.bancoalimentos.service.impl;

import com.remington.bancoalimentos.model.entity.*;
import com.remington.bancoalimentos.model.repository.*;
import com.remington.bancoalimentos.service.BusinessRuleException;
import com.remington.bancoalimentos.service.EntregaService;
import com.remington.bancoalimentos.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la legalización y despacho de entregas a beneficiarios.
 * <p>
 * Aplica transaccionalidad atómica y valida las siguientes reglas de negocio del ECS-01:
 * </p>
 * <ul>
 *   <li><b>RN-12:</b> Cobertura exclusiva para beneficiarios validados en estratos socioeconómicos 1, 2 y 3.</li>
 *   <li><b>RN-16:</b> Control preventivo de inventario para evitar sobregiros de existencias antes de emitir el acta.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Service
public class EntregaServiceImpl implements EntregaService {

    private final EntregaRepository entregaRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService;

    /**
     * Constructor con inyección de repositorios y del servicio de inventario.
     *
     * @param entregaRepository      Repositorio de entregas.
     * @param beneficiarioRepository Repositorio de beneficiarios.
     * @param usuarioRepository      Repositorio de usuarios.
     * @param productoRepository     Repositorio de productos.
     * @param inventarioService      Servicio para decremento del stock disponible.
     */
    public EntregaServiceImpl(EntregaRepository entregaRepository, BeneficiarioRepository beneficiarioRepository,
                              UsuarioRepository usuarioRepository, ProductoRepository productoRepository,
                              InventarioService inventarioService) {
        this.entregaRepository = entregaRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.inventarioService = inventarioService;
    }

    /**
     * Formaliza una entrega a un comedor comunitario o familia vulnerable, descontando existencias en bodega.
     *
     * @param idBeneficiario Identificador del beneficiario que solicita la ayuda.
     * @param idUsuario      Identificador del funcionario responsable del despacho.
     * @param fecha          Fecha de expedición y entrega.
     * @param ruta           Código formal del acta o ruta de entrega.
     * @param idProducto     Identificador del producto a entregar.
     * @param cantidad       Cantidad solicitada a entregar.
     * @param observaciones  Observaciones o constancia de entrega.
     * @return Entidad {@link Entrega} persistida con su respectivo {@link DetalleEntrega}.
     * @throws BusinessRuleException Si el beneficiario pertenece a estratos 4..6 (RN-12) o si hay sobregiro (RN-16).
     */
    @Override
    @Transactional
    public Entrega registrarEntregaConDetalle(Long idBeneficiario, Long idUsuario, LocalDate fecha,
                                            String ruta, Long idProducto, BigDecimal cantidad, String observaciones) {
        Beneficiario ben = beneficiarioRepository.findById(idBeneficiario)
                .orElseThrow(() -> new BusinessRuleException("Beneficiario no encontrado: " + idBeneficiario));

        // RN-12: Exclusivo Estratos 1 a 3
        if (ben.getEstratoSocioeconomico() == null || ben.getEstratoSocioeconomico() < 1 || ben.getEstratoSocioeconomico() > 3) {
            throw new BusinessRuleException("RN-12: El beneficiario debe pertenecer exclusivamente a estratos 1, 2 o 3.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new BusinessRuleException("Usuario responsable no encontrado: " + idUsuario));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new BusinessRuleException("Producto no encontrado: " + idProducto));

        // RN-16: Validar y descontar stock antes de persistir la entrega
        inventarioService.descontarStock(idProducto, cantidad);

        Entrega entrega = new Entrega();
        entrega.setBeneficiario(ben);
        entrega.setUsuarioResponsable(usuario);
        entrega.setFechaEntrega(fecha != null ? fecha : LocalDate.now());
        entrega.setRutaEntrega(ruta != null ? ruta : "Ruta General Medellín");
        entrega.setObservacionesEvidencia(observaciones);
        entrega.setEstado("ACTIVO");

        DetalleEntrega det = new DetalleEntrega();
        det.setProducto(producto);
        det.setCantidad(cantidad);
        det.setEstado("ACTIVO");

        entrega.addDetalle(det);
        return entregaRepository.save(entrega);
    }

    /**
     * Retorna todas las entregas legalizadas en el sistema.
     *
     * @return Lista de entregas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Entrega> listarTodas() {
        return entregaRepository.findAll();
    }
}
