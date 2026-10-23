package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.config;

import com.projecto.gestiondeadmisiones.domain.model.exceptions.ArchivoInseguroException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.BusinessException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.CorreoYaRegistradoException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.CredencialesInvalidasException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.DocumentosIncompletosException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.EscaneoArchivoNoDisponibleException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.EstadoInscripcionInvalidoException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionYaExisteException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.RequisitosNoCumplidosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

// Rutas funcionales (RouterFunction/Handler): un RestControllerAdvice con ExceptionHandler
// nunca se invoca aquí, solo intercepta controladores anotados. Este WebExceptionHandler es
// el equivalente funcional, registrado con prioridad sobre el DefaultErrorWebExceptionHandler
// de Spring Boot (Order -1).
@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolverEstado(ex);
        String codigo = ex.getClass().getSimpleName();
        String mensaje = ex.getMessage();

        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error("Error no controlado atendiendo {}", exchange.getRequest().getPath(), ex);
            codigo = "ERROR_INTERNO";
            mensaje = "Ha ocurrido un error interno. Intente nuevamente más tarde.";
        }

        return JsonErrorWriter.escribir(exchange, status, codigo, mensaje);
    }

    private HttpStatus resolverEstado(Throwable ex) {
        if (ex instanceof CorreoYaRegistradoException
                || ex instanceof InscripcionYaExisteException
                || ex instanceof DocumentosIncompletosException
                || ex instanceof EstadoInscripcionInvalidoException) {
            return HttpStatus.CONFLICT;
        }
        if (ex instanceof CredencialesInvalidasException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof RequisitosNoCumplidosException) {
            return HttpStatus.FORBIDDEN;
        }
        if (ex instanceof InscripcionNoEncontradaException) {
            return HttpStatus.NOT_FOUND;
        }
        if (ex instanceof ArchivoInseguroException) {
            return HttpStatus.UNPROCESSABLE_CONTENT;
        }
        if (ex instanceof EscaneoArchivoNoDisponibleException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        if (ex instanceof IllegalArgumentException || ex instanceof BusinessException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
