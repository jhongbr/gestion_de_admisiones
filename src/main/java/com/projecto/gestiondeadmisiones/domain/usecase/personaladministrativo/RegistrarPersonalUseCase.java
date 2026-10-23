package com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.CorreoYaRegistradoException;
import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import com.projecto.gestiondeadmisiones.domain.model.personal.gateway.PersonalAdministrativoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegistrarPersonalUseCase {

    private final PersonalAdministrativoRepository personalAdministrativoRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<PersonalAdministrativo> ejecutar(PersonalAdministrativo personalAdministrativo, String passwordPlano) {
        return personalAdministrativoRepository.existePorCorreo(personalAdministrativo.getCorreoElectronico())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new CorreoYaRegistradoException(personalAdministrativo.getCorreoElectronico()));
                    }

                    PersonalAdministrativo nuevoPersonal = personalAdministrativo.toBuilder()
                            .passwordHash(passwordEncoderPort.codificar(passwordPlano))
                            .fechaCreacionCuenta(LocalDateTime.now())
                            .build();

                    return personalAdministrativoRepository.guardar(nuevoPersonal);
                });
    }
}
