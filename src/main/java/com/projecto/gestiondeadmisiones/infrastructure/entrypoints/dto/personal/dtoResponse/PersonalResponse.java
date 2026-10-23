package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonalResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String rol;
}
