package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class ArchivoInseguroException extends BusinessException {

    public ArchivoInseguroException() {
        super("El archivo no superó el escaneo antivirus y no fue almacenado");
    }
}
