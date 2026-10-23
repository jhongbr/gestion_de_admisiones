package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class EstadoInscripcionResponseDTO {

    private Long id;
    private String numeroInscripcion;
    private String estado;
    private String programa;
    private String semestre;
    private LocalDateTime fechaCreacion;
    private boolean documentoIdentidadCargado;
    private boolean fotoCargada;
    private String referenciaPago;
    private LocalDate fechaPago;
}
