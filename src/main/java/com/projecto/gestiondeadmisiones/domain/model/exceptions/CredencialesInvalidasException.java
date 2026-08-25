package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class CredencialesInvalidasException extends BusinessException{

    public CredencialesInvalidasException() {
        super("Correo electrónico o contraseña incorrectos");
    }
}
