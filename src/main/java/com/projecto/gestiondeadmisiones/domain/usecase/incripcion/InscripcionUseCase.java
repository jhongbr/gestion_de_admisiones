package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;


import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.InscripcionYaExisteException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.NumeroInscripcionDuplicadoException;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.RequisitosNoCumplidosException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class InscripcionUseCase {

    private static final int MAX_INTENTOS_NUMERO_INSCRIPCION = 5;

    private final InscripcionRepository inscripcionRepository;

    public Mono<Inscripcion> ejecutar(Inscripcion inscripcion) {
        if(!inscripcion.isCumpleTituloBachiller() || !inscripcion.isCumpleIcfes()){
            return Mono.error(new RequisitosNoCumplidosException());
        }
        return inscripcionRepository.existePorAspiranteId(inscripcion.getAspiranteId())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new InscripcionYaExisteException());
                    }
                    return guardarConNumeroUnico(inscripcion);
                });
    }

    private Mono<Inscripcion> guardarConNumeroUnico(Inscripcion inscripcion) {
        return Mono.defer(() -> {
                    Inscripcion nuevaInscripcion = inscripcion.toBuilder()
                            .numeroInscripcion(generarCodigoInscripcion())
                            .estadoInscripcion(EstadoInscripcion.PENDIENTE)
                            .fechaCreacion(LocalDateTime.now())
                            .build();
                    return inscripcionRepository.guardar(nuevaInscripcion);
                })
                .retryWhen(Retry.max(MAX_INTENTOS_NUMERO_INSCRIPCION)
                        .filter(NumeroInscripcionDuplicadoException.class::isInstance));
    }

    private String generarCodigoInscripcion() {
        int aleatorio = (int) (Math.random() * 100000);
        return String.format("INS-%05d", aleatorio);
    }
}
