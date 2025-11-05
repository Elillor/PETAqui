package com.buscadorpelut.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración de Spring Security para la aplicación.
 * 
 * Define:
 * - El algoritmo de codificación de contraseñas (BCrypt).
 * - Las reglas de autorización para los endpoints.
 * - La política de sesiones y desactivación de características no utilizadas (como formLogin).
 */
@Configuration
public class SecurityConfig {
    
    /**
     * Define el codificador de contraseñas utilizado para encriptar las contraseñas de los usuarios.
     * BCrypt es un algoritmo seguro y resistente a ataques de fuerza bruta.
     * 
     * @return Instancia de PasswordEncoder basada en BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     * 
     * - Desactiva CSRF para utilizar otro mecanismo seguro.
     * - Permite acceso sin autenticación a recursos estáticos y a todos los endpoints bajo "/api/**".
     * - Desactiva formLogin y HTTP Basic, asumiendo que la autenticación se manejará de forma personalizada
     *   desde el frontend.
     * - Usa la política de sesión IF_REQUIRED (puede crearse una sesión si otro filtro la necesita).
     * 
     * @param http Objeto HttpSecurity para configurar las reglas de seguridad.
     * @return SecurityFilterChain configurado.
     * @throws Exception si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain FilterChain (HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("login.html","index.html","/css/**","/js/**","/img/**","/favicon.ico").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }

}
