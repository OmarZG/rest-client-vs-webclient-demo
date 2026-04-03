package org.zgo.cuentacorriente.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida para exponer los datos de una cuenta corriente.
 * Record Java 17 — serializable por Jackson sin configuración extra.
 */
public record CuentaCorrienteResponse(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal limiteCredito,
        String banco,
        LocalDate fechaApertura
) {
}
