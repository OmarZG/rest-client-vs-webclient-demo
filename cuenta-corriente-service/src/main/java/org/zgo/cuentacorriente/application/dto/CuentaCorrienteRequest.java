package org.zgo.cuentacorriente.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de entrada para crear una cuenta corriente.
 * Record Java 17 — inmutable y sin boilerplate.
 */
public record CuentaCorrienteRequest(
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal limiteCredito,
        String banco,
        LocalDate fechaApertura
) {
}
