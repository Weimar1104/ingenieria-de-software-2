package com.remington.bancoalimentos.controller;

import com.remington.bancoalimentos.service.InventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal para el panel de control y la página de inicio del sistema.
 * <p>
 * Gestiona el mapeo de la ruta raíz ("/") cargando las existencias físicas en tiempo real
 * del inventario de bodega central conforme a las especificaciones de las Historias de Usuario
 * HU-29 a HU-32 y la Regla de Negocio RN-07.
 * </p>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Controller
public class HomeController {

    private final InventarioService inventarioService;

    /**
     * Constructor con inyección del servicio de inventario.
     *
     * @param inventarioService Servicio de lógica de negocio para existencias de bodega.
     */
    public HomeController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    /**
     * Atiende la petición GET para la página principal del sistema.
     * Carga la lista completa de productos en inventario y los suministra a la plantilla Thymeleaf.
     *
     * @param model Objeto {@link Model} de Spring MVC para transportar atributos a la vista.
     * @return El nombre lógico de la vista HTML "index".
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("inventarios", inventarioService.listarTodo());
        return "index";
    }
}
