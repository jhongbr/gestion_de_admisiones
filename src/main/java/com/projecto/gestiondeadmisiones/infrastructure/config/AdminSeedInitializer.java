package com.projecto.gestiondeadmisiones.infrastructure.config;

import com.projecto.gestiondeadmisiones.domain.model.enums.RolPersonal;
import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import com.projecto.gestiondeadmisiones.domain.model.personal.gateway.PersonalAdministrativoRepository;
import com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo.RegistrarPersonalUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminSeedInitializer implements ApplicationRunner {

    private final PersonalAdministrativoRepository personalAdministrativoRepository;
    private final RegistrarPersonalUseCase registrarPersonalUseCase;
    private final String correoSeed;
    private final String passwordSeed;

    public AdminSeedInitializer(PersonalAdministrativoRepository personalAdministrativoRepository,
                                 RegistrarPersonalUseCase registrarPersonalUseCase,
                                 @Value("${app.admin-seed.correo:}") String correoSeed,
                                 @Value("${app.admin-seed.password:}") String passwordSeed) {
        this.personalAdministrativoRepository = personalAdministrativoRepository;
        this.registrarPersonalUseCase = registrarPersonalUseCase;
        this.correoSeed = correoSeed;
        this.passwordSeed = passwordSeed;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (correoSeed.isBlank() || passwordSeed.isBlank()) {
            return;
        }

        boolean yaExiste = Boolean.TRUE.equals(
                personalAdministrativoRepository.existePorCorreo(correoSeed).block());
        if (yaExiste) {
            return;
        }

        PersonalAdministrativo admin = PersonalAdministrativo.builder()
                .nombres("Administrador")
                .apellidos("Inicial")
                .correoElectronico(correoSeed)
                .rol(RolPersonal.ADMISIONES)
                .build();

        registrarPersonalUseCase.ejecutar(admin, passwordSeed).block();
        log.info("Usuario administrativo inicial creado para el correo {}", correoSeed);
    }
}
