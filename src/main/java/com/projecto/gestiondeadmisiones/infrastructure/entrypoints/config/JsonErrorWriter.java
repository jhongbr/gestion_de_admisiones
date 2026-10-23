package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Único punto donde se serializa el cuerpo {"codigo", "mensaje"} que usan tanto el
 * GlobalExceptionHandler (errores de dominio) como los handlers de seguridad (401/403 de
 * Spring Security), para que todas las respuestas de error salgan en el mismo formato.
 */
public final class JsonErrorWriter {

    private JsonErrorWriter() {
    }

    public static Mono<Void> escribir(ServerWebExchange exchange, HttpStatus status, String codigo, String mensaje) {
        byte[] cuerpo = serializar(codigo, mensaje);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(cuerpo);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private static byte[] serializar(String codigo, String mensaje) {
        String json = "{\"codigo\":\"" + escapar(codigo) + "\",\"mensaje\":\"" + escapar(mensaje) + "\"}";
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private static String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", " ").replace("\r", " ");
    }
}
