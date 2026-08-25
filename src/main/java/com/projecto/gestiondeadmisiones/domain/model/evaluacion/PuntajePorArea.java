package com.projecto.gestiondeadmisiones.domain.model.evaluacion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PuntajePorArea {

    private Integer matematica;
    private Integer lecturaCritica;
    private Integer cienciasNaturales;
    private Integer ingles;
}
