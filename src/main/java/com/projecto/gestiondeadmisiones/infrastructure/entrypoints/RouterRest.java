package com.projecto.gestiondeadmisiones.infrastructure.entrypoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler){
        return route(
                POST("/api/aspirantes/registro"), handler::registrarAspirante)
                .andRoute(POST("/api/aspirantes/login"), handler::loginAspirante)
                .andRoute(POST("/api/personal/registro"), handler::registrarPersonal)
                .andRoute(POST("/api/personal/login"), handler::loginPersonal)
                .andRoute(POST("/api/inscripciones"), handler::crearInscripcion)
                .andRoute(POST("/api/inscripciones/documentos"), handler::adjuntarDocumentos)
                .andRoute(POST("/api/inscripciones/pago"), handler::registrarPago)
                .andRoute(GET("/api/inscripciones/mi-estado"), handler::consultarEstadoInscripcion)
                .andRoute(GET("/api/admin/inscripciones/pendientes"), handler::listarInscripcionesPendientes)
                .andRoute(GET("/api/admin/inscripciones/{id}"), handler::obtenerDetalleInscripcion)
                .andRoute(POST("/api/tesoreria/inscripciones/{id}/validar-pago"), handler::validarPago)
                .andRoute(POST("/api/evaluacion/inscripciones/{id}/evaluar"), handler::evaluarInscripcion);

    }
}
