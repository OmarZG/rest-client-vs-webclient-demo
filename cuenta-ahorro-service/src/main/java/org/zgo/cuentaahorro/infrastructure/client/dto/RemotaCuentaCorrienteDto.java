package org.zgo.cuentaahorro.infrastructure.client.dto;

import java.math.BigDecimal;

/**
 * DTO para deserializar la respuesta de cuenta-corriente-service.
 */
public record RemotaCuentaCorrienteDto(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal limiteCredito,
        String banco
) {
}
