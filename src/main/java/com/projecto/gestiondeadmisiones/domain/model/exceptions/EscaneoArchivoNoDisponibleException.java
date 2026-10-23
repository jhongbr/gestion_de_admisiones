package com.projecto.gestiondeadmisiones.domain.model.exceptions;

public class EscaneoArchivoNoDisponibleException extends BusinessException {

    public EscaneoArchivoNoDisponibleException() {
        super("No fue posible validar el archivo con el antivirus en este momento. Intente nuevamente más tarde.");
    }
}
