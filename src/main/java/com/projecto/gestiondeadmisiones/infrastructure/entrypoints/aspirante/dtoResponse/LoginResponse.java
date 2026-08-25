package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String token;
    private String tipo;
}
