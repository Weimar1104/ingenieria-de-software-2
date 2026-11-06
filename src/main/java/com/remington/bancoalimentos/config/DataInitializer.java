package com.remington.bancoalimentos.config;

import com.remington.bancoalimentos.model.entity.*;
import com.remington.bancoalimentos.model.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Componente de inicialización y siembra de datos de arranque (Data Seeder).
 * <p>
 * Implementa {@link CommandLineRunner} para ejecutarse automáticamente al iniciar la aplicación.
 * Garantiza la existencia de registros esenciales para pruebas y validación del corte vertical (Vertical Slice),
 * incluyendo:
 * </p>
 * <ul>
 *   <li>Roles institucionales del sistema (Administrador, Coordinador Donaciones, Responsable Inventario, Coordinador Entregas).</li>
 *   <li>Usuarios preconfigurados con contraseñas encriptadas mediante BCrypt.</li>
 *   <li>Donante comercial activo (RN-04).</li>
 *   <li>Catálogo base de productos no perecederos.</li>
 *   <li>Existencias iniciales en inventario central (RN-07).</li>
 *   <li>Beneficiario comunitario validado en estrato socioeconómico 2 (RN-12).</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final DonanteRepository donanteRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final BeneficiarioRepository beneficiarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias para los repositorios de entidades y el codificador de contraseñas.
     *
     * @param rolRepository          Repositorio para gestión de roles.
     * @param usuarioRepository      Repositorio para gestión de usuarios.
     * @param donanteRepository      Repositorio para gestión de donantes.
     * @param productoRepository     Repositorio para gestión de catálogo de alimentos.
     * @param inventarioRepository   Repositorio para control de existencias en bodega.
     * @param beneficiarioRepository Repositorio para registro de beneficiarios comunitarios.
     * @param passwordEncoder        Codificador de contraseñas BCrypt.
     */
    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                           DonanteRepository donanteRepository, ProductoRepository productoRepository,
                           InventarioRepository inventarioRepository, BeneficiarioRepository beneficiarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.donanteRepository = donanteRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.beneficiarioRepository = beneficiarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Ejecuta la lógica de siembra de datos únicamente si la base de datos se encuentra vacía.
     *
     * @param args Argumentos pasados al inicio de la aplicación.
     * @throws Exception Si ocurre algún error en la persistencia de datos.
     */
    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) {
            Rol adminRol = rolRepository.save(new Rol("Administrador del Sistema", "Gestión general y accesos"));
            Rol donRol = rolRepository.save(new Rol("Coordinador de Donaciones", "Recepción y donantes"));
            Rol invRol = rolRepository.save(new Rol("Responsable de Inventario", "Bodega y existencias"));
            Rol entRol = rolRepository.save(new Rol("Coordinador de Entregas", "Beneficiarios y despacho"));

            Usuario u1 = new Usuario();
            u1.setRol(adminRol);
            u1.setNombreCompleto("Albert Higuita Bautista");
            u1.setCorreoElectronico("albert.higuita@remington.edu.co");
            u1.setContrasenaHash(passwordEncoder.encode("admin123"));
            usuarioRepository.save(u1);

            Usuario u2 = new Usuario();
            u2.setRol(donRol);
            u2.setNombreCompleto("Weimar Esteban Serna");
            u2.setCorreoElectronico("weimar.serna@remington.edu.co");
            u2.setContrasenaHash(passwordEncoder.encode("donaciones123"));
            usuarioRepository.save(u2);
        }

        if (donanteRepository.count() == 0) {
            Donante d = new Donante();
            d.setNombreRazonSocial("Supermercados Éxito Medellín");
            d.setTipoDonante("Supermercado");
            d.setUbicacion("Cra 48 # 32B Sur, Medellín");
            d.setTelefonoContacto("6043310000");
            d.setCorreoContacto("donaciones@exito.com");
            donanteRepository.save(d);
        }

        if (productoRepository.count() == 0) {
            Producto p1 = productoRepository.save(new Producto("Arroz Blanco Diana", "Granos y Cereales", "Kg"));
            Producto p2 = productoRepository.save(new Producto("Fríjol Cargamanto", "Granos y Cereales", "Kg"));
            Producto p3 = productoRepository.save(new Producto("Aceite Vegetal", "Aceites y Grasas", "Litros"));

            inventarioRepository.save(new Inventario(p1, new BigDecimal("120.00")));
            inventarioRepository.save(new Inventario(p2, new BigDecimal("85.00")));
            inventarioRepository.save(new Inventario(p3, new BigDecimal("40.00")));
        }

        if (beneficiarioRepository.count() == 0) {
            Beneficiario b = new Beneficiario();
            b.setNombreBeneficiario("Comedor Comunitario San Javier");
            b.setTipoBeneficiario("Comedor Comunitario");
            b.setEstratoSocioeconomico(2); // RN-12: Exclusivo estratos 1, 2 y 3
            b.setUbicacionComuna("Comuna 13 - San Javier");
            b.setTelefonoContacto("3014455667");
            b.setTipoAyudaSolicitada("Raciones secas y granos");
            beneficiarioRepository.save(b);
        }
    }
}
