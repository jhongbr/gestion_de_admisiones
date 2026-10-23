package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class InscripcionDetalleResponseDTO {

    private Long id;
    private String numeroInscripcion;
    private String estado;
    private String observacion;

    private String correoAspirante;
    private String nombresAspirante;
    private String apellidosAspirante;

    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String ciudadResidencia;
    private String institucionBachillerato;
    private String municipioColegio;
    private int anioGraduacion;
    private String codigoResultadoIcfes;
    private int puntajeGlobalIcfes;
    private String facultad;
    private String programa;
    private String semestre;

    private String documentoIdentidadUrl;
    private String fotoUrl;
    private String referenciaPago;
    private LocalDate fechaPago;
    private String bancoPago;

    private LocalDateTime fechaCreacion;
}
