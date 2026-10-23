package com.projecto.gestiondeadmisiones.domain.usecase.aspirante;


import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.CorreoYaRegistradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RegistrarAspiranteUseCase {

    private final AspiranteRepository aspiranteRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Mono<Aspirante> ejecutar(Aspirante aspirante, String passwordPlano) {
        return aspiranteRepository.existePorCorreo(aspirante.getCorreoElectronico())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new CorreoYaRegistradoException(aspirante.getCorreoElectronico()));
                    }

                    Aspirante nuevoAspirante = aspirante.toBuilder()
                            .passwordHash(passwordEncoderPort.codificar(passwordPlano))
                            .fechaCreacionCuenta(LocalDateTime.now())
                            .build();

                    return aspiranteRepository.guardar(nuevoAspirante);
                });
    }

}
