package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.aspirante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "aspirante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AspiranteEntity {

    @Id
    private Long id;

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private String genero;
    private String correoElectronico;
    private String passwordHash;
    private String telefono;
    private String ciudadResidencia;

    private String institucionBachillerato;
    private String ciudadColegio;
    private Integer anioGraduacion;
    private Integer resultadoIcfes;

    private LocalDateTime fechaCreacionCuenta;

}
