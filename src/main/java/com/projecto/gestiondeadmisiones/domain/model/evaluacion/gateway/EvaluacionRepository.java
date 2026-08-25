package com.projecto.gestiondeadmisiones.domain.model.evaluacion.gateway;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoEvaluacion;
import com.projecto.gestiondeadmisiones.domain.model.evaluacion.Evaluacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EvaluacionRepository {

    Mono<Evaluacion> guardar(Evaluacion evaluacion);
    Mono<Evaluacion> buscarPorId(Long id);
    Mono<Evaluacion> buscarPorInscripcionId(Long inscripcionId);
    Flux<Evaluacion> buscarPorEstado(EstadoEvaluacion estado);
}
