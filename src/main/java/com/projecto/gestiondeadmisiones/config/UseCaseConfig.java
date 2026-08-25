package com.projecto.gestiondeadmisiones.config;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.IniciarSesionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.RegistrarAspiranteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarAspiranteUseCase registrarAspiranteUseCase(AspiranteRepository aspiranteRepository,
                                                               PasswordEncoderPort passwordEncoderPort) {
        return new RegistrarAspiranteUseCase(aspiranteRepository, passwordEncoderPort);
    }

    @Bean
    public IniciarSesionUseCase iniciarSesionUseCase(AspiranteRepository aspiranteRepository,
                                                     PasswordEncoderPort passwordEncoderPort) {
        return new IniciarSesionUseCase(aspiranteRepository, passwordEncoderPort);
    }
}
