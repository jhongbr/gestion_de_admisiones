package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.inscripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.NumeroInscripcionDuplicadoException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import io.r2dbc.spi.R2dbcException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InscripcionRepositoryAdapter implements InscripcionRepository {

    private final InscripcionR2dbcRepository inscripcionR2dbcRepository;
    private final InscripcionMapper inscripcionMapper;

    @Override
    public Mono<Inscripcion> guardar(Inscripcion inscripcion) {
        InscripcionEntity entity = inscripcionMapper.toEntity(inscripcion);
        return inscripcionR2dbcRepository.save(entity)
                .map(inscripcionMapper::toDomain)
                .onErrorMap(this::esViolacionDeNumeroDuplicado, e -> new NumeroInscripcionDuplicadoException());
    }

    private boolean esViolacionDeNumeroDuplicado(Throwable e) {
        if (e instanceof DataIntegrityViolationException) {
            return true;
        }
        if (e instanceof R2dbcException && e.getMessage() != null) {
            return e.getMessage().toLowerCase().contains("numero_inscripcion");
        }
        return false;
    }

    @Override
    public Mono<Inscripcion> buscarPorId(Long id) {
        return inscripcionR2dbcRepository.findById(id)
                .map(inscripcionMapper::toDomain);
    }

    @Override
    public Mono<Inscripcion> buscarPorNumeroInscripcion(String numeroInscripcion) {
        return inscripcionR2dbcRepository.findByNumeroInscripcion(numeroInscripcion)
                .map(inscripcionMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existePorAspiranteId(Long aspiranteId) {
        return inscripcionR2dbcRepository.existsByAspiranteId(aspiranteId);
    }

    @Override
    public Flux<Inscripcion> buscarPorEstado(EstadoInscripcion estado) {
        return inscripcionR2dbcRepository.findByEstado(estado.name())
                .map(inscripcionMapper::toDomain);
    }

    @Override
    public Flux<Inscripcion> buscarTodas() {
        return inscripcionR2dbcRepository.findAll()
                .map(inscripcionMapper::toDomain);
    }

    @Override
    public Flux<Inscripcion> buscarNoFinalizadas() {
        return inscripcionR2dbcRepository.findByEstadoNotIn(
                        List.of(EstadoInscripcion.APROBADO.name(), EstadoInscripcion.RECHAZADO.name()))
                .map(inscripcionMapper::toDomain);
    }

    @Override
    public Mono<Inscripcion> buscarPorAspiranteId(Long aspiranteId) {
        return inscripcionR2dbcRepository.findByAspiranteId(aspiranteId)
                .map(inscripcionMapper::toDomain);
    }
}
