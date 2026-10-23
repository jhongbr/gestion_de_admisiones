package com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.CredencialesInvalidasException;
import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import com.projecto.gestiondeadmisiones.domain.model.personal.gateway.PersonalAdministrativoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class IniciarSesionPersonalUseCase {

    private final PersonalAdministrativoRepository personalAdministrativoRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<PersonalAdministrativo> ejecutar(String correoElectronico, String passwordPlano) {
        return personalAdministrativoRepository.obtenerPorCorreo(correoElectronico)
                .filter(personal -> passwordEncoderPort.verificar(passwordPlano, personal.getPasswordHash()))
                .switchIfEmpty(Mono.error(new CredencialesInvalidasException()));
    }
}
