package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class InscripcionYaExisteException extends BusinessException {

    public InscripcionYaExisteException() {
        super("el estudiante ya tiene una inscripción activa");
    }
}
