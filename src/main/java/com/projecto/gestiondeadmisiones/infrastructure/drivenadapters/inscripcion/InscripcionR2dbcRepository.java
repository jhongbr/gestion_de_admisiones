package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.inscripcion;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface InscripcionR2dbcRepository extends ReactiveCrudRepository<InscripcionEntity, Long> {
        Mono<InscripcionEntity> findByAspiranteId(Long aspiranteId);
        Mono<Boolean> existsByAspiranteId(Long aspiranteId);
        Mono<InscripcionEntity> findByNumeroInscripcion(String numeroInscripcion);
        Flux<InscripcionEntity> findByEstado(String estado);
        Flux<InscripcionEntity> findByEstadoNotIn(Collection<String> estados);

}


