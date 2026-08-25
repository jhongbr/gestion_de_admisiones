package com.projecto.gestiondeadmisiones.domain.model.pago.gateway;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoPago;
import com.projecto.gestiondeadmisiones.domain.model.pago.Pago;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PagoRepository {

    Mono<Pago> guardar(Pago pago);
    Mono<Pago> buscarPorId(Long id);
    Mono<Pago> buscarPorInscripcionId(Long inscripcionId);
    Flux<Pago> buscarPorEstado(EstadoPago estado);
}
