package org.zgo.cuentacorriente.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/* 
 * Entidad JPA para persistencia de Cuenta Corriente.
 */
@Entity
@Table(name = "cuenta_corriente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCorrienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "limite_credito", precision = 15, scale = 2)
    private BigDecimal limiteCredito;

    @Column(nullable = false, length = 50)
    private String banco;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;
}
