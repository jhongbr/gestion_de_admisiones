package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.aspirante.dtoRequest;

import lombok.Getter;

@Getter
public class DatosAcademicosRequest {

    private String institucionBachillerato;
    private String municipioColegio;
    private int anioGraduacion;
    private String codigoResultadoIcfes;
    private int puntajeGlobalIcfes;

}
