package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ValidarPagoRequestDTO {

    @NotNull(message = "Debe indicar si el pago fue aprobado")
    private Boolean aprobado;

    private String observacion;
}
