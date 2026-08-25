package com.projecto.gestiondeadmisiones.infrastructure.entrypoints;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;


import com.projecto.gestiondeadmisiones.domain.model.exceptions.CredencialesInvalidasException;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.IniciarSesionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.RegistrarAspiranteUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.RegistroAspiranteComando;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoResponse.AspiranteResponse;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoRequest.LoginRequest;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoResponse.LoginResponse;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoRequest.ResgitroAspiranteRequest;
import com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt.JwtProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RegistrarAspiranteUseCase registrarAspiranteUseCase;
    private final IniciarSesionUseCase iniciarSesionUseCase;
    private final JwtProvider jwtProvider;
    private final Validator validator;

    // ---------- Aspirante: registro ----------

    public Mono<ServerResponse> registrarAspirante(ServerRequest request) {
        return request.bodyToMono(ResgitroAspiranteRequest.class)
                .flatMap(this::validar)
                .map(this::aComandoRegistro)
                .flatMap(registrarAspiranteUseCase::ejecutar)
                .flatMap(aspirante -> ServerResponse
                        .ok()
                        .bodyValue(aRespuesta(aspirante)))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }

    // ---------- Aspirante: login ----------

    public Mono<ServerResponse> loginAspirante(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::validar)
                .flatMap(dto -> iniciarSesionUseCase.ejecutar(dto.getCorreoElectronico(), dto.getContrasena()))
                .flatMap(aspirante -> {
                    String token = jwtProvider.generarToken(aspirante.getId(), aspirante.getCorreoElectronico());
                    return ServerResponse.ok().bodyValue(
                            LoginResponse.builder().token(token).tipo("Bearer").build());
                })
                .onErrorResume(CredencialesInvalidasException.class,
                        ex -> ServerResponse.status(401).bodyValue(ex.getMessage()))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> ServerResponse.badRequest().bodyValue(ex.getMessage()));
    }

    // A medida que agreguemos inscripción, pago, documentos, evaluación,
    // sus métodos van entrando aquí mismo, junto a los de aspirante.

    private <T> Mono<T> validar(T dto) {
        Set<ConstraintViolation<T>> violaciones = validator.validate(dto);
        if (!violaciones.isEmpty()) {
            String mensaje = violaciones.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return Mono.error(new IllegalArgumentException(mensaje));
        }
        return Mono.just(dto);
    }

    private RegistroAspiranteComando aComandoRegistro(ResgitroAspiranteRequest dto) {
        return RegistroAspiranteComando.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correoElectronico(dto.getCorreoElectronico())
                .contrasenaPlana(dto.getContrasena())
                .build();
    }

    private AspiranteResponse aRespuesta(Aspirante aspirante) {
        return AspiranteResponse.builder()
                .id(aspirante.getId())
                .nombres(aspirante.getNombres())
                .apellidos(aspirante.getApellidos())
                .correoElectronico(aspirante.getCorreoElectronico())
                .build();
    }
}
