package com.projecto.gestiondeadmisiones.infrastructure.entrypoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler){
        return route(
                POST("/api/aspirantes/registro"), handler::registrarAspirante)
                .andRoute(POST("/api/aspirantes/login"), handler::loginAspirante);
    }
}
