package com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway;

import reactor.core.publisher.Mono;

public interface EscanerArchivosPort {

    Mono<Boolean> esSeguro(byte[] contenido);
}
