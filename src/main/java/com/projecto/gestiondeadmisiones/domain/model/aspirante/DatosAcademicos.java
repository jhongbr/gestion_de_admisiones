package com.projecto.gestiondeadmisiones.domain.model.aspirante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatosAcademicos {

    private String institucionBachillerato;
    private String ciudadColegio;
    private Integer anioGraduacion;
    private String resultadoIcfes;
}
