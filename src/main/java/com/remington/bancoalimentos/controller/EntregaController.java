package com.remington.bancoalimentos.controller;

import com.remington.bancoalimentos.model.repository.BeneficiarioRepository;
import com.remington.bancoalimentos.model.repository.ProductoRepository;
import com.remington.bancoalimentos.model.repository.UsuarioRepository;
import com.remington.bancoalimentos.service.BusinessRuleException;
import com.remington.bancoalimentos.service.EntregaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controlador web Spring MVC responsable de la gestión de despachos y entregas de alimentos a beneficiarios.
 * <p>
 * Atiende las interacciones de los requisitos funcionales RF-33 a RF-36 e historias de usuario HU-33 a HU-36,
 * aplicando estrictamente:
 * </p>
 * <ul>
 *   <li>RN-12: Exclusividad de asignación a beneficiarios clasificados en estratos 1, 2 y 3.</li>
 *   <li>RN-16: Control de existencias físicas para impedir sobregiros de stock en bodega.</li>
 *   <li>Reducción automática de inventario disponible tras legalizar la entrega.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Controller
@RequestMapping("/entregas")
public class EntregaController {

    private final EntregaService entregaService;
    private final BeneficiarioRepository beneficiarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias para el servicio de entregas y los repositorios de soporte.
     *
     * @param entregaService         Servicio de lógica de negocio para despacho comunitario.
     * @param beneficiarioRepository Repositorio de beneficiarios autorizados.
     * @param productoRepository     Repositorio de productos alimenticios.
     * @param usuarioRepository      Repositorio de funcionarios del banco.
     */
    public EntregaController(EntregaService entregaService, BeneficiarioRepository beneficiarioRepository,
                             ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.entregaService = entregaService;
        this.beneficiarioRepository = beneficiarioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Despliega la interfaz de despacho de entregas mostrando beneficiarios activos, productos e histórico.
     *
     * @param model Objeto {@link Model} para alimentar la vista Thymeleaf.
     * @return El nombre lógico de la plantilla "entregas".
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entregas", entregaService.listarTodas());
        model.addAttribute("beneficiarios", beneficiarioRepository.findByEstado("ACTIVO"));
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "entregas";
    }

    /**
     * Procesa la solicitud POST para autorizar y legalizar un despacho de alimentos comunitarios.
     *
     * @param idBeneficiario         Identificador del beneficiario que recibe (RN-12: Estratos 1..3).
     * @param idUsuario              Identificador del funcionario responsable de autorizar la entrega.
     * @param fecha                  Fecha de formalización del acta (opcional, por defecto fecha actual).
     * @param rutaParam              Ruta física o sector de destino bajo el parámetro "ruta".
     * @param codigoActaParam        Código formal del acta de entrega bajo el parámetro "codigoActa".
     * @param idProducto             Identificador del producto a despachar.
     * @param cantidadParam          Cantidad a entregar bajo el parámetro estándar "cantidad" (RN-16).
     * @param cantidadEntregadaParam Cantidad a entregar bajo el alias "cantidadEntregada".
     * @param observaciones          Notas de trazabilidad o evidencia de la entrega comunitaria.
     * @param flash                  Gestor de mensajes flash para retroalimentación tras redirección.
     * @return Redirección a la ruta GET "/entregas".
     */
    @PostMapping("/guardar")
    public String guardar(@RequestParam Long idBeneficiario,
                          @RequestParam Long idUsuario,
                          @RequestParam(required = false) String fecha,
                          @RequestParam(name = "ruta", required = false) String rutaParam,
                          @RequestParam(name = "codigoActa", required = false) String codigoActaParam,
                          @RequestParam Long idProducto,
                          @RequestParam(name = "cantidad", required = false) BigDecimal cantidadParam,
                          @RequestParam(name = "cantidadEntregada", required = false) BigDecimal cantidadEntregadaParam,
                          @RequestParam(required = false) String observaciones,
                          RedirectAttributes flash) {
        try {
            BigDecimal cantidad = cantidadParam != null ? cantidadParam : cantidadEntregadaParam;
            if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                flash.addFlashAttribute("error", "La cantidad a entregar debe ser mayor a cero.");
                return "redirect:/entregas";
            }

            String rutaFinal = (codigoActaParam != null && !codigoActaParam.isBlank()) 
                    ? ("Acta: " + codigoActaParam.trim()) 
                    : (rutaParam != null && !rutaParam.isBlank() ? rutaParam.trim() : "Ruta General Medellín");

            LocalDate f = (fecha != null && !fecha.isBlank()) ? LocalDate.parse(fecha) : LocalDate.now();
            entregaService.registrarEntregaConDetalle(idBeneficiario, idUsuario, f, rutaFinal, idProducto, cantidad, observaciones);
            flash.addFlashAttribute("success", "¡Despacho legalizado con éxito y stock descontado de la bodega!");
        } catch (BusinessRuleException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error inesperado al programar entrega: " + e.getMessage());
        }
        return "redirect:/entregas";
    }
}
