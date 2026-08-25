package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    };
}
