package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.aspirante;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AspiranteR2dbcRepository extends ReactiveCrudRepository<AspiranteEntity, Long > {

    Mono<AspiranteEntity> findByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
