package org.zgo.cuentaahorro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/* 
 * Entidad JPA para persistencia de Cuenta de Ahorro.
 */
@Entity
@Table(name = "cuenta_ahorro")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaAhorroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "tasa_interes_anual", precision = 5, scale = 2)
    private BigDecimal tasaInteresAnual;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;
}
