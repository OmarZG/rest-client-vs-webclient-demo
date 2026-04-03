package org.zgo.cuentaahorro.domain.model;

import java.math.BigDecimal;

/* 
 * Value Object de dominio. Representa la información que cuenta-ahorro-service
 * obtiene de cuenta-corriente-service sobre una Cuenta Corriente.
 */
public record SaldoCorrienteInfo(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal limiteCredito,
        String banco
) {
}
