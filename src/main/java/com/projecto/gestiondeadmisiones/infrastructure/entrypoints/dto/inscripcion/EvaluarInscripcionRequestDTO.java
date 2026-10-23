package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EvaluarInscripcionRequestDTO {

    @NotBlank(message = "La decisión es obligatoria (APROBADO o RECHAZADO)")
    private String decision;

    private String observacion;
}
