package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.personal;

import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import com.projecto.gestiondeadmisiones.domain.model.personal.gateway.PersonalAdministrativoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PersonalAdministrativoRepositoryAdapter implements PersonalAdministrativoRepository {

    private final PersonalAdministrativoR2dbcRepository personalAdministrativoR2dbcRepository;
    private final PersonalAdministrativoMapper mapper;

    @Override
    public Mono<PersonalAdministrativo> guardar(PersonalAdministrativo personalAdministrativo) {
        return personalAdministrativoR2dbcRepository.save(mapper.toEntity(personalAdministrativo))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<PersonalAdministrativo> obtenerPorCorreo(String correoElectronico) {
        return personalAdministrativoR2dbcRepository.findByCorreoElectronico(correoElectronico)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existePorCorreo(String correoElectronico) {
        return personalAdministrativoR2dbcRepository.existsByCorreoElectronico(correoElectronico);
    }
}
