package com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginPersonalRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
