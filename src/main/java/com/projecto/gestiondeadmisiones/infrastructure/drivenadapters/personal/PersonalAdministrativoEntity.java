package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.personal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "personal_administrativo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAdministrativoEntity {

    @Id
    private Long id;
    private String nombres;
    private String apellidos;
    private String correoElectronico;
    private String passwordHash;
    private String rol;
    private LocalDateTime fechaCreacionCuenta;
}
