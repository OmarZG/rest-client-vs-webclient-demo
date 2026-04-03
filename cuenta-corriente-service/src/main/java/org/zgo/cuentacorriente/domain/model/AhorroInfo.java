package org.zgo.cuentacorriente.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Value Object de dominio. Representa los datos que Micro-B
 * obtiene de Micro-A sobre una Cuenta de Ahorro.
 * Es un Record (inmutable) porque es resultado de una consulta externa.
 */
public record AhorroInfo(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal tasaInteresAnual,
        LocalDate fechaApertura
) {
}
