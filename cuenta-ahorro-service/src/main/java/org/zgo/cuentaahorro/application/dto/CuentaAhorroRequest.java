package org.zgo.cuentaahorro.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para crear una cuenta de ahorro.
 */
public record CuentaAhorroRequest(
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal tasaInteresAnual,
        LocalDate fechaApertura
) {
}
