package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RegistrarPagoRequestDTO {

    private String referenciaPago;
    private LocalDate fechaPago;
    private String bancoPago;
    private boolean confirmacionVeracidad;
}
