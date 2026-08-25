package com.projecto.gestiondeadmisiones.domain.model.aspirante;

import com.projecto.gestiondeadmisiones.domain.model.enums.Genero;
import com.projecto.gestiondeadmisiones.domain.model.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Aspirante {

    private Long id;
    private String nombres;
    private String apellidos;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private Genero genero;
    private String correoElectronico;
    private String passwordHash;
    private String telefono;
    private String ciudadResidencia;
    private LocalDate fechaNacimiento;
    private DatosAcademicos datosAcademicos;
    private LocalDateTime fechaCreacionCuenta;

}
