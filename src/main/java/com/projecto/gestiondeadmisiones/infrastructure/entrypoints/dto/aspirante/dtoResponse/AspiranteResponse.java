package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.aspirante.dtoResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AspiranteResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
}
