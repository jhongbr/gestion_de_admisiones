package com.projecto.gestiondeadmisiones.domain.model.documentos.gateway;

import com.projecto.gestiondeadmisiones.domain.model.documentos.Documento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentosRepository {

    Mono<Documento> guardar(Documento documento);
    Mono<Documento> buscarPorId(Long id);
    Flux<Documento> buscarPorInscripcionId(Long inscripcionId);
}
