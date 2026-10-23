package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InscripcionResponseDTO {

        private Long id;
        private String numeroInscripcion;
        private String estado;
        private String programa;
        private String semestre;
}
