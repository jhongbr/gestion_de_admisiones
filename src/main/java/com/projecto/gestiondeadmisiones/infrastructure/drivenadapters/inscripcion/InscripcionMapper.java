package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.inscripcion;

import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {
    @Mapping(target = "estadoInscripcion", source = "estado")
    Inscripcion toDomain(InscripcionEntity entity);
    @Mapping(target = "estado", source = "estadoInscripcion")
    InscripcionEntity toEntity(Inscripcion domain);
}
