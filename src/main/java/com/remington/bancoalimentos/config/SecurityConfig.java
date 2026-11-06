package com.remington.bancoalimentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad perimetral y control de acceso basado en roles (RBAC).
 * <p>
 * Implementa las directrices de seguridad documentadas en el proyecto técnico:
 * </p>
 * <ul>
 *   <li>Cifrado irreversible de credenciales mediante el algoritmo {@link BCryptPasswordEncoder}.</li>
 *   <li>Gestión de identidades con roles diferenciados: ADMIN, COORDINADOR_DONACIONES, RESPONSABLE_INVENTARIO.</li>
 *   <li>Protección contra ataques CSRF en todas las peticiones mutables del sistema.</li>
 *   <li>Políticas de autorización de rutas con redirección automática al formulario de inicio de sesión.</li>
 * </ul>
 *
 * @author Albert Higuita Bautista
 * @author Weimar Esteban Serna Asprilla
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define el bean codificador de contraseñas utilizando el algoritmo BCrypt con factor de costo estándar.
     *
     * @return Instancia de {@link PasswordEncoder} basada en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el servicio de usuarios en memoria con credenciales semilla para pruebas inmediatas
     * según los perfiles de usuario estipulados en los casos de uso del documento.
     *
     * @param encoder Codificador de contraseñas inyectado.
     * @return Gestor de detalles de usuario {@link UserDetailsService}.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails donaciones = User.builder()
                .username("coordinador")
                .password(encoder.encode("donaciones123"))
                .roles("COORDINADOR_DONACIONES")
                .build();

        UserDetails inventario = User.builder()
                .username("bodega")
                .password(encoder.encode("bodega123"))
                .roles("RESPONSABLE_INVENTARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, donaciones, inventario);
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP, definiendo las reglas de autorización,
     * el formulario de autenticación predeterminado y la sesión de usuario.
     *
     * @param http Objeto {@link HttpSecurity} para construir las políticas.
     * @return Cadena de filtros compilada {@link SecurityFilterChain}.
     * @throws Exception Si ocurre un fallo en la construcción de los filtros.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
