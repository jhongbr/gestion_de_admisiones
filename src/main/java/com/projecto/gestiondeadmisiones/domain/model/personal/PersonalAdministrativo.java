package com.projecto.gestiondeadmisiones.domain.model.personal;

import com.projecto.gestiondeadmisiones.domain.model.enums.RolPersonal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PersonalAdministrativo {

    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String passwordHash;
    private RolPersonal rol;
    private LocalDateTime fechaCreacionCuenta;
}
