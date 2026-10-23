package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConsultarEstadoInscripcionUseCase {

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Long aspiranteId) {
        return inscripcionRepository.buscarPorAspiranteId(aspiranteId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()));
    }
}
