package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JsonAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return JsonErrorWriter.escribir(exchange, HttpStatus.FORBIDDEN,
                "ACCESO_DENEGADO", "No tiene permisos para acceder a este recurso");
    }
}
