package com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InscripcionRepository {

    Mono<Inscripcion> guardar(Inscripcion inscripcion);
    Mono<Inscripcion> buscarPorId(Long id);
    Mono<Inscripcion> buscarPorNumeroInscripcion(String numeroInscripcion);
    Mono<Boolean> existePorAspiranteId(Long aspiranteId);
    Flux<Inscripcion> buscarPorEstado(EstadoInscripcion estado);
    Flux<Inscripcion> buscarTodas();
    Flux<Inscripcion> buscarNoFinalizadas();
    Mono<Inscripcion> buscarPorAspiranteId(Long aspiranteId);
}
