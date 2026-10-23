package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

// Incluye todo estado no final (ni APROBADO ni RECHAZADO), no solo PENDIENTE.
@RequiredArgsConstructor
public class ListarInscripcionesPendientesUseCase {

    private final InscripcionRepository inscripcionRepository;
    private final AspiranteRepository aspiranteRepository;

    public Flux<InscripcionConAspirante> ejecutar() {
        return inscripcionRepository.buscarNoFinalizadas()
                .flatMap(inscripcion -> aspiranteRepository.obtenerPorId(inscripcion.getAspiranteId())
                        .map(aspirante -> new InscripcionConAspirante(inscripcion, aspirante)));
    }
}
