package org.zgo.cuentaahorro.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida para exponer los datos de una cuenta de ahorro.
 */
public record CuentaAhorroResponse(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal tasaInteresAnual,
        LocalDate fechaApertura
) {
}
