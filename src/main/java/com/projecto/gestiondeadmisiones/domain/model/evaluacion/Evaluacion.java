package com.projecto.gestiondeadmisiones.domain.model.evaluacion;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoEvaluacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Evaluacion {

    private Long id;
    private Long inscripcionId;
    private Integer puntajeGlobal;
    private PuntajePorArea puntajePorArea;
    private String observaciones;
    private EstadoEvaluacion estadoEvaluacion;
}
