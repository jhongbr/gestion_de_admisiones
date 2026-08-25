package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.aspirante;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.DatosAcademicos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AspiranteMapper {

    @Mapping(target = "datosAcademicos", source = ".")
    Aspirante toDomain(AspiranteEntity entity);

    @Mapping(target = "institucionBachillerato", source = "datosAcademicos.institucionBachillerato")
    @Mapping(target = "ciudadColegio", source = "datosAcademicos.ciudadColegio")
    @Mapping(target = "anioGraduacion", source = "datosAcademicos.anioGraduacion")

    @Mapping(target = "resultadoIcfes", source = "datosAcademicos.resultadoIcfes")
    AspiranteEntity toEntity(Aspirante domain);

    DatosAcademicos toDatosAcademicos(AspiranteEntity entity);
}
