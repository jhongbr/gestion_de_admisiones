package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class InscripcionNoEncontradaException extends BusinessException {
    public InscripcionNoEncontradaException() {
        super("Inscripcion no encontrada");
    }
}
