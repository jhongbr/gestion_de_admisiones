package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.inscripcion;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "inscripcion")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InscripcionEntity {

    @Id
    private Long id;
    private Long aspiranteId;
    private String numeroInscripcion;
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
    private Integer anioGraduacion;
    private String codigoResultadoIcfes;
    private Integer puntajeGlobalIcfes;
    private String facultad;
    private String programa;
    private String semestre;
    private String documentoIdentidadUrl;
    private String fotoUrl;
    private String referenciaPago;
    private LocalDate fechaPago;
    private String bancoPago;
    private String observacion;
    private String estado;
    private LocalDateTime fechaCreacion;
}
