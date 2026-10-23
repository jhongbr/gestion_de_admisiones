package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.EstadoInscripcionInvalidoException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionNoEncontradaException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

// Tesorería valida la referencia de pago registrada por el aspirante.
// EN_REVISION -> ENVIADO (pago correcto) o EN_REVISION -> INCOMPLETO (pago rechazado).
@RequiredArgsConstructor
public class ValidarPagoUseCase {

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Long inscripcionId, boolean aprobado, String observacion) {
        return inscripcionRepository.buscarPorId(inscripcionId)
                .switchIfEmpty(Mono.error(new InscripcionNoEncontradaException()))
                .flatMap(inscripcion -> {
                    if (inscripcion.getEstadoInscripcion() != EstadoInscripcion.EN_REVISION) {
                        return Mono.error(new EstadoInscripcionInvalidoException(
                                "Solo se puede validar el pago de inscripciones en estado EN_REVISION"));
                    }
                    Inscripcion actualizada = inscripcion.toBuilder()
                            .estadoInscripcion(aprobado ? EstadoInscripcion.ENVIADO : EstadoInscripcion.INCOMPLETO)
                            .observacion(observacion)
                            .build();
                    return inscripcionRepository.guardar(actualizada);
                });
    }
}
