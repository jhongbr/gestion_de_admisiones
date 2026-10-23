package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class RequisitosNoCumplidosException extends BusinessException {
    public RequisitosNoCumplidosException() {
        super("No se cumplen los requisitos para hacer la inscripcion");
    }
}
