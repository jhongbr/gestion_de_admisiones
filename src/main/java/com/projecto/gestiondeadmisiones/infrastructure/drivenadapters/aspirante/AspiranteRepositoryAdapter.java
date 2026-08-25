package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.aspirante;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AspiranteRepositoryAdapter implements AspiranteRepository {

    private final AspiranteR2dbcRepository aspiranteR2dbcRepository;
    private final AspiranteMapper mapper;

    @Override
    public Mono<Aspirante> guardar(Aspirante aspirante) {
        return aspiranteR2dbcRepository.save(mapper.toEntity(aspirante)).map(mapper::toDomain);}

    @Override
    public Mono<Aspirante> obtenerPorNumeroDocumento(String numeroDocumento) {
        return null;
    }

    @Override
    public Mono<Aspirante> obtenerPorId(Long id) {
        return aspiranteR2dbcRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Aspirante> obtenerPorCorreo(String correoElectronico) {
        return aspiranteR2dbcRepository.findByCorreoElectronico(correoElectronico).map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existePorCorreo(String correoElectronico) {
        return aspiranteR2dbcRepository.existsByCorreoElectronico(correoElectronico);
    }
}
