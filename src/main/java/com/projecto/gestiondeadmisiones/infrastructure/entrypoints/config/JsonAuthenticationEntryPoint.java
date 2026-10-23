package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JsonAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return JsonErrorWriter.escribir(exchange, HttpStatus.UNAUTHORIZED,
                "NO_AUTENTICADO", "Debe autenticarse para acceder a este recurso");
    }
}
