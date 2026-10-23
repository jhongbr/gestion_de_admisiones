package com.projecto.gestiondeadmisiones.config;

import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config.JsonAccessDeniedHandler;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config.JsonAuthenticationEntryPoint;
import com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt.JwtReactiveAuthenticationManager;
import com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt.JwtServerAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtReactiveAuthenticationManager authenticationManager,
            JwtServerAuthenticationConverter authenticationConverter,
            JsonAccessDeniedHandler accessDeniedHandler,
            JsonAuthenticationEntryPoint authenticationEntryPoint) {

        AuthenticationWebFilter jwtAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        jwtAuthenticationFilter.setServerAuthenticationConverter(authenticationConverter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/aspirantes/registro", "/api/aspirantes/login", "/api/personal/login").permitAll()
                        .pathMatchers("/api/personal/registro").hasRole("ADMISIONES")
                        .pathMatchers("/api/tesoreria/inscripciones/*/validar-pago").hasRole("TESORERIA")
                        .pathMatchers("/api/evaluacion/inscripciones/*/evaluar").hasRole("EVALUACION")
                        .pathMatchers("/api/admin/inscripciones/pendientes", "/api/admin/inscripciones/*")
                                .hasAnyRole("ADMISIONES", "TESORERIA", "EVALUACION")
                        .pathMatchers("/api/inscripciones", "/api/inscripciones/documentos",
                                "/api/inscripciones/pago", "/api/inscripciones/mi-estado").hasRole("ASPIRANTE")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of("http://localhost:3000"));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        configuracion.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }
}
