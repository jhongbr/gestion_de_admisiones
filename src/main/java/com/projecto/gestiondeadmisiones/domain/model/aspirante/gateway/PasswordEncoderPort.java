package com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway;

public interface PasswordEncoderPort {

    String codificar(String passwordPlano);
    boolean verificar(String passwordPlano, String passwordHash);
}
