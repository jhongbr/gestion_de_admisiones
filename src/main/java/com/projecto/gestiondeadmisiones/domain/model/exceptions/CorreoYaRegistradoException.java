package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class CorreoYaRegistradoException extends BusinessException {

    public CorreoYaRegistradoException(String correo) {
        super("El correo electrónico " + correo + " ya está registrado.");
    }
}
