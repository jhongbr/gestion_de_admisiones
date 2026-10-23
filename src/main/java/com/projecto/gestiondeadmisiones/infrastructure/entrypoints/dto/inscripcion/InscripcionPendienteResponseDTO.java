package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InscripcionPendienteResponseDTO {

    private Long inscripcionId;
    private String numeroInscripcion;
    private String estado;
    private String correoAspirante;
    private String nombresAspirante;
    private String apellidosAspirante;
    private LocalDateTime fechaCreacion;
}
