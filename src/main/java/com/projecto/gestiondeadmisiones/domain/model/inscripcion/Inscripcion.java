package com.projecto.gestiondeadmisiones.domain.model.inscripcion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Inscripcion {

    private Long id;
    private String numeroInscripcion;
    private Long aspiranteId;
    private ProgramaAcademico programaAcademico;
    private String semestre;
    private LocalDate fechaInscripcion;
    private EstadoInscripcion estadoInscripcion;
}
