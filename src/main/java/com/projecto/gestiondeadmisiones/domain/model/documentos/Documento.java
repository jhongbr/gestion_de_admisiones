package com.projecto.gestiondeadmisiones.domain.model.documentos;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoDocumento;
import com.projecto.gestiondeadmisiones.domain.model.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Documento {

    private Long id;
    private Long inscripcionId;
    private TipoDocumento tipoDocumento;
    private String nombreArchivo;
    private LocalDate fechaSubida;
    private EstadoDocumento estadoDocumento;
}
