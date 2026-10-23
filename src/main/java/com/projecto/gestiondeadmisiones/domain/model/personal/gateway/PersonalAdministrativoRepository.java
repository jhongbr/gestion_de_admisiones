package com.projecto.gestiondeadmisiones.domain.model.personal.gateway;

import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import reactor.core.publisher.Mono;

public interface PersonalAdministrativoRepository {

    Mono<PersonalAdministrativo> guardar(PersonalAdministrativo personalAdministrativo);
    Mono<PersonalAdministrativo> obtenerPorCorreo(String correoElectronico);
    Mono<Boolean> existePorCorreo(String correoElectronico);
}
