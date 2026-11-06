package com.remington.bancoalimentos.controller;

import com.remington.bancoalimentos.model.repository.DonanteRepository;
import com.remington.bancoalimentos.model.repository.ProductoRepository;
import com.remington.bancoalimentos.model.repository.UsuarioRepository;
import com.remington.bancoalimentos.service.BusinessRuleException;
import com.remington.bancoalimentos.service.DonacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controlador web Spring MVC encargado de la gestión y recepción de donaciones de alimentos.
 * <p>
 * Implementa el flujo de interacción para los requisitos funcionales RF-21 a RF-28 e historias
 * de usuario HU-21 a HU-28, aplicando las siguientes directrices técnicas:
 * </p>
 * <ul>
 *   <li>RN-04: Exclusividad de registro sobre donantes activos.</li>
 *   <li>RN-14: Evaluación de calidad sanitaria (APTO vs RECHAZADO) en el ingreso.</li>
 *   <li>Actualización automática del stock físico en bodega para alimentos aptos.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Controller
@RequestMapping("/donaciones")
public class DonacionController {

    private final DonacionService donacionService;
    private final DonanteRepository donanteRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor con inyección de dependencias para el servicio de donaciones y los repositorios de soporte.
     *
     * @param donacionService    Servicio de lógica de negocio para donaciones.
     * @param donanteRepository  Repositorio para consulta de donantes.
     * @param productoRepository Repositorio para consulta de productos disponibles.
     * @param usuarioRepository  Repositorio para consulta de funcionarios del sistema.
     */
    public DonacionController(DonacionService donacionService, DonanteRepository donanteRepository,
                              ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.donacionService = donacionService;
        this.donanteRepository = donanteRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Despliega la vista principal del módulo de donaciones con el formulario de captura y el historial.
     *
     * @param model Objeto {@link Model} para inyectar donaciones registradas, donantes, productos y usuarios.
     * @return El nombre lógico de la vista "donaciones".
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("donaciones", donacionService.listarTodas());
        model.addAttribute("donantes", donanteRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "donaciones";
    }

    /**
     * Procesa la solicitud POST para legalizar una donación e inspección fitosanitaria.
     * Soporta los alias de parámetros de formulario para garantizar compatibilidad y robustez ante envíos web.
     *
     * @param idDonante             Identificador único del donante remitente (RN-04).
     * @param idUsuario             Identificador del funcionario receptor responsable.
     * @param fecha                 Cadena de texto con la fecha de la donación (opcional, por defecto fecha actual).
     * @param idProducto            Identificador del producto o alimento recibido.
     * @param cantidadParam         Cantidad recibida bajo el parámetro estándar "cantidad".
     * @param cantidadRecibidaParam Cantidad recibida bajo el alias "cantidadRecibida".
     * @param estadoAlimentoParam   Estado sanitario bajo el parámetro estándar "estadoAlimento" (APTO/RECHAZADO).
     * @param estadoCalidadParam    Estado sanitario bajo el alias "estadoCalidad".
     * @param numeroLote            Número de lote asignado por el fabricante o proveedor.
     * @param observaciones         Notas adicionales sobre la entrega o condiciones de embalaje.
     * @param flash                 Atributos flash para enviar mensajes de éxito o error tras la redirección.
     * @return Redirección a la ruta GET "/donaciones".
     */
    @PostMapping("/guardar")
    public String guardar(@RequestParam Long idDonante,
                          @RequestParam Long idUsuario,
                          @RequestParam(required = false) String fecha,
                          @RequestParam Long idProducto,
                          @RequestParam(name = "cantidad", required = false) BigDecimal cantidadParam,
                          @RequestParam(name = "cantidadRecibida", required = false) BigDecimal cantidadRecibidaParam,
                          @RequestParam(name = "estadoAlimento", required = false) String estadoAlimentoParam,
                          @RequestParam(name = "estadoCalidad", required = false) String estadoCalidadParam,
                          @RequestParam(required = false) String numeroLote,
                          @RequestParam(required = false) String observaciones,
                          RedirectAttributes flash) {
        try {
            BigDecimal cantidad = cantidadParam != null ? cantidadParam : cantidadRecibidaParam;
            if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                flash.addFlashAttribute("error", "La cantidad donada debe ser mayor a cero.");
                return "redirect:/donaciones";
            }

            String estadoFinal = (estadoCalidadParam != null && !estadoCalidadParam.isBlank()) 
                    ? estadoCalidadParam 
                    : (estadoAlimentoParam != null ? estadoAlimentoParam : "APTO");

            StringBuilder obsBuilder = new StringBuilder();
            if (numeroLote != null && !numeroLote.isBlank()) {
                obsBuilder.append("[Lote: ").append(numeroLote.trim()).append("] ");
            }
            if (observaciones != null && !observaciones.isBlank()) {
                obsBuilder.append(observaciones.trim());
            }

            LocalDate f = (fecha != null && !fecha.isBlank()) ? LocalDate.parse(fecha) : LocalDate.now();
            donacionService.registrarDonacionConDetalle(idDonante, idUsuario, f, idProducto, cantidad, estadoFinal, obsBuilder.toString());
            flash.addFlashAttribute("success", "¡Donación registrada con éxito y stock ingresado a inventario en bodega!");
        } catch (BusinessRuleException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error inesperado al registrar donación: " + e.getMessage());
        }
        return "redirect:/donaciones";
    }
}
