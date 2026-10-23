package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginPersonalResponse {

    private String token;
    private String tipo;
    private String rol;
}
