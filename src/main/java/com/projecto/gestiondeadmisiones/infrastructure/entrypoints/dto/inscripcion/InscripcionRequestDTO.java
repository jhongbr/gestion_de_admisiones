package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InscripcionRequestDTO {

    private Boolean cumpleTituloBachiller;
    private Boolean cumpleIcfes;
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
}
