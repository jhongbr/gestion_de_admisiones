package com.projecto.gestiondeadmisiones.domain.usecase.aspirante;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistroAspiranteComando {

    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String contrasenaPlana;
}
