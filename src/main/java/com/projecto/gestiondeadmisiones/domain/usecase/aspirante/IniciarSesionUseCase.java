package com.projecto.gestiondeadmisiones.domain.usecase.aspirante;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class IniciarSesionUseCase {

    private final AspiranteRepository aspiranteRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<Aspirante> ejecutar(String correoElectronico, String passwordPlano){
        return aspiranteRepository.obtenerPorCorreo(correoElectronico)
                .filter(aspirante -> passwordEncoderPort.verificar(passwordPlano, aspirante.getPasswordHash()))
                .switchIfEmpty(Mono.error(new RuntimeException("Correo o contraseña incorrectos")));
    }
}
