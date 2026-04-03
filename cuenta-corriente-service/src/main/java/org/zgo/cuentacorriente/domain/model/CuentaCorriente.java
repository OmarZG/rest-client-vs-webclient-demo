package org.zgo.cuentacorriente.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Modelo de dominio puro de una Cuenta Corriente.
 * Sin dependencias de JPA, Spring ni infraestructura.
 * Contexto bounded del Micro-B (cuenta-corriente-service).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorriente {

    private Long id;
    private String numeroCuenta;
    private String titular;
    private BigDecimal saldo;
    /** Límite de crédito disponible en la cuenta corriente */
    private BigDecimal limiteCredito;
    /** Banco emisor de la cuenta corriente */
    private String banco;
    private LocalDate fechaApertura;
}
