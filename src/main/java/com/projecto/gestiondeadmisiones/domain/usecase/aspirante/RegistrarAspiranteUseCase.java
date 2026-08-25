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

    public Mono<Aspirante> ejecutar(RegistroAspiranteComando comando) {
        return aspiranteRepository.existePorCorreo(comando.getCorreoElectronico())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new CorreoYaRegistradoException(comando.getCorreoElectronico()));
                    }

                    String contrasenaHasheada = passwordEncoderPort.codificar(comando.getContrasenaPlana());

                    Aspirante nuevoAspirante = Aspirante.builder()
                            .nombres(comando.getNombres())
                            .apellidos(comando.getApellidos())
                            .correoElectronico(comando.getCorreoElectronico())
                            .passwordHash(contrasenaHasheada)
                            .fechaCreacionCuenta(LocalDateTime.now())
                            .build();

                    return aspiranteRepository.guardar(nuevoAspirante);
                });
    }

}
