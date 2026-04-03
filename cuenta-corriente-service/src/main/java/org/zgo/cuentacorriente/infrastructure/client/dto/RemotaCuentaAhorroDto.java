package org.zgo.cuentacorriente.infrastructure.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO interno de infraestructura para deserializar la respuesta HTTP de Micro-A.
 * No sale de la capa de infraestructura. El adaptador lo convierte al domain VO (AhorroInfo).
 */
public record RemotaCuentaAhorroDto(
        Long id,
        String numeroCuenta,
        String titular,
        BigDecimal saldo,
        BigDecimal tasaInteresAnual,
        LocalDate fechaApertura
) {
}
