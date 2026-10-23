package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class DocumentosIncompletosException extends BusinessException {
    public DocumentosIncompletosException() {
        super("Debe adjuntar el documento de identidad y la foto antes de registrar el pago");
    }
}
