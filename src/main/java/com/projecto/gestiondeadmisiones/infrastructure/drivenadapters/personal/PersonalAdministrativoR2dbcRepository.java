package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.personal;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonalAdministrativoR2dbcRepository extends ReactiveCrudRepository<PersonalAdministrativoEntity, Long> {

    Mono<PersonalAdministrativoEntity> findByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
