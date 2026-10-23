package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

// Detalle completo de una inscripción (para los paneles de Admisiones, Tesorería y Evaluación).
@RequiredArgsConstructor
public class ObtenerDetalleInscripcionUseCase {

    private final InscripcionRepository inscripcionRepository;
    private final AspiranteRepository aspiranteRepository;

    public Mono<InscripcionConAspirante> ejecutar(Long inscripcionId) {
        return inscripcionRepository.buscarPorId(inscripcionId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()))
                .flatMap(inscripcion -> aspiranteRepository.obtenerPorId(inscripcion.getAspiranteId())
                        .map(aspirante -> new InscripcionConAspirante(inscripcion, aspirante)));
    }
}
