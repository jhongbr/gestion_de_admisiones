package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.DocumentosIncompletosException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
public class RegistrarPagoUseCase {

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Long aspiranteId, String referenciaPago, LocalDate fechaPago, String bancoPago) {
        return inscripcionRepository.buscarPorAspiranteId(aspiranteId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()))
                .flatMap(inscripcion -> {
                    if (inscripcion.getDocumentoIdentidadUrl() == null || inscripcion.getFotoUrl() == null) {
                        return Mono.error(new DocumentosIncompletosException());
                    }

                    Inscripcion actualizada = inscripcion.toBuilder()
                            .referenciaPago(referenciaPago)
                            .fechaPago(fechaPago)
                            .bancoPago(bancoPago)
                            .estadoInscripcion(EstadoInscripcion.EN_REVISION)
                            .build();

                    return inscripcionRepository.guardar(actualizada);
                });
    }
}
