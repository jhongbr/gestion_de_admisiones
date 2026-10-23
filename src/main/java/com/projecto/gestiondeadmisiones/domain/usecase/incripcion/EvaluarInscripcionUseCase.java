package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.EstadoInscripcionInvalidoException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

// Evaluación decide el resultado final de la admisión.
// ENVIADO -> APROBADO o ENVIADO -> RECHAZADO.
@RequiredArgsConstructor
public class EvaluarInscripcionUseCase {

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Long inscripcionId, EstadoInscripcion decision, String observacion) {
        if (decision != EstadoInscripcion.APROBADO && decision != EstadoInscripcion.RECHAZADO) {
            return Mono.error(new IllegalArgumentException("La decisión debe ser APROBADO o RECHAZADO"));
        }
        return inscripcionRepository.buscarPorId(inscripcionId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()))
                .flatMap(inscripcion -> {
                    if (inscripcion.getEstadoInscripcion() != EstadoInscripcion.ENVIADO) {
                        return Mono.error(new EstadoInscripcionInvalidoException(
                                "Solo se pueden evaluar inscripciones en estado ENVIADO"));
                    }
                    Inscripcion actualizada = inscripcion.toBuilder()
                            .estadoInscripcion(decision)
                            .observacion(observacion)
                            .build();
                    return inscripcionRepository.guardar(actualizada);
                });
    }
}
