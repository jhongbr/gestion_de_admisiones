package com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import reactor.core.publisher.Mono;

public interface AspiranteRepository {

    Mono<Aspirante> guardar(Aspirante aspirante);
    Mono<Aspirante> obtenerPorId(Long id);
    Mono<Aspirante> obtenerPorNumeroDocumento(String numeroDocumento);
    Mono<Aspirante> obtenerPorCorreo(String correo);
    Mono<Boolean> existePorCorreo(String correo);
}
