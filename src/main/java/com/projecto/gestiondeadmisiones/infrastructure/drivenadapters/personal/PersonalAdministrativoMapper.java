package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.personal;

import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalAdministrativoMapper {

    PersonalAdministrativo toDomain(PersonalAdministrativoEntity entity);
    PersonalAdministrativoEntity toEntity(PersonalAdministrativo domain);
}
