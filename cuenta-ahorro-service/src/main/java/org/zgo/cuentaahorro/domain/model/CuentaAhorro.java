package org.zgo.cuentaahorro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 * Modelo de dominio puro. No tiene ninguna dependencia de JPA, Spring ni infraestructura.
 * Representa una Cuenta de Ahorro dentro del bounded context del Micro-A.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaAhorro {

    private Long id;
    private String numeroCuenta;
    private String titular;
    private BigDecimal saldo;
    /** Tasa de interés anual en porcentaje, ej. 4.25 = 4.25% */
    private BigDecimal tasaInteresAnual;
    private LocalDate fechaApertura;
}
