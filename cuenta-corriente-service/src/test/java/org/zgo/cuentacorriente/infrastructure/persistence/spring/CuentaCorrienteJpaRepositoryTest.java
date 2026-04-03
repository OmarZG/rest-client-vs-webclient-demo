package org.zgo.cuentacorriente.infrastructure.persistence.spring;

import org.zgo.cuentacorriente.infrastructure.persistence.entity.CuentaCorrienteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest: Carga solo el contexto de JPA + H2 en memoria.
 * No levanta el servidor web ni beans de servicio.
 * Valida que el filtrado y paginación funcionan correctamente en H2.
 */
@DataJpaTest
@DisplayName("CuentaCorriente JPA Repository - Pruebas de paginación y persistencia")
class CuentaCorrienteJpaRepositoryTest {

    @Autowired
    private CuentaCorrienteJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        String[] bancos = {"BBVA", "Banamex", "Santander", "HSBC", "Banorte"};
        for (int i = 1; i <= 7; i++) {
            repository.save(CuentaCorrienteEntity.builder()
                    .numeroCuenta("001-CTE-" + String.format("%03d", i))
                    .titular("Empresa S.A. " + i)
                    .saldo(new BigDecimal(i * 5000))
                    .limiteCredito(new BigDecimal(i * 10000))
                    .banco(bancos[i % bancos.length])
                    .fechaApertura(LocalDate.of(2023, i, 1))
                    .build());
        }
    }

    @Test
    @DisplayName("Debe devolver exactamente 5 registros en primera página sobre 7 totales")
    void testPaginacion_primeraPageDe5_sobre7Registros() {
        Page<CuentaCorrienteEntity> page = repository.findAll(PageRequest.of(0, 5));

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(7);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
    }

    @Test
    @DisplayName("Segunda página debe tener los 2 registros restantes")
    void testPaginacion_segundaPagina_tieneResiduo() {
        Page<CuentaCorrienteEntity> page = repository.findAll(PageRequest.of(1, 5));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.isLast()).isTrue();
    }

    @Test
    @DisplayName("Guardar y recuperar una CuentaCorriente conserva limiteCredito y banco")
    void testSave_conservaCamposEspecificosDominio() {
        CuentaCorrienteEntity entity = CuentaCorrienteEntity.builder()
                .numeroCuenta("999-CTE-TEST")
                .titular("Corp S.A. de C.V.")
                .saldo(new BigDecimal("50000.00"))
                .limiteCredito(new BigDecimal("100000.00"))
                .banco("BBVA")
                .fechaApertura(LocalDate.of(2025, 3, 1))
                .build();

        CuentaCorrienteEntity guardada = repository.save(entity);

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getLimiteCredito()).isEqualByComparingTo("100000.00");
        assertThat(guardada.getBanco()).isEqualTo("BBVA");
        assertThat(guardada.getNumeroCuenta()).isEqualTo("999-CTE-TEST");
    }
}
