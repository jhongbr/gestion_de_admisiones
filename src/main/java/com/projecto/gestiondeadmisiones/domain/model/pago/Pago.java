package com.projecto.gestiondeadmisiones.domain.model.pago;

import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoPago;
import com.projecto.gestiondeadmisiones.domain.model.enums.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Pago {

    private Long id;
    private Long inscripcionId;
    private BigDecimal monto;
    private String referenciaPago;
    private String entidadBancaria;
    private LocalDate fechaPago;
    private MetodoPago metodoPago;
    private EstadoPago estadoPago;
}
