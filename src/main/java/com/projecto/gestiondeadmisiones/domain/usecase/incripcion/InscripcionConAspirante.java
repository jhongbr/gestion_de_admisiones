package com.projecto.gestiondeadmisiones.domain.usecase.incripcion;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;

public record InscripcionConAspirante(Inscripcion inscripcion, Aspirante aspirante) {
}
