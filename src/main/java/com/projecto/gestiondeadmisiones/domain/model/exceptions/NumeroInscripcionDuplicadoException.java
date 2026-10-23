package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class NumeroInscripcionDuplicadoException extends BusinessException {

    public NumeroInscripcionDuplicadoException() {
        super("El número de inscripción generado ya existe");
    }
}
