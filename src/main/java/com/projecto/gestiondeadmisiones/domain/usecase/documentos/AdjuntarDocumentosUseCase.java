package com.projecto.gestiondeadmisiones.domain.usecase.documentos;

import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AdjuntarDocumentosUseCase {

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Long aspiranteId, String documentoIdentidadUrl, String fotoUrl) {
        return inscripcionRepository.buscarPorAspiranteId(aspiranteId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()))
                .flatMap(inscripcion -> {
                    Inscripcion actualizada = inscripcion.toBuilder()
                            .documentoIdentidadUrl(documentoIdentidadUrl)
                            .fotoUrl(fotoUrl)
                            .build();
                    return inscripcionRepository.guardar(actualizada);
                });
    }
}
