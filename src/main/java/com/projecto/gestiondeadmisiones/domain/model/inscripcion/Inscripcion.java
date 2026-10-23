package com.projecto.gestiondeadmisiones.domain.model.inscripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Inscripcion {

    private Long id;
    private Long aspiranteId;
    private String numeroInscripcion;
    private boolean cumpleTituloBachiller;
    private boolean cumpleIcfes;
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
    private String observacion;
    private EstadoInscripcion estadoInscripcion;
    private LocalDateTime fechaCreacion;
}
