package org.zgo.cuentaahorro.infrastructure.persistence.spring;

import org.zgo.cuentaahorro.infrastructure.persistence.entity.CuentaAhorroEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de capa de persistencia con @DataJpaTest (contexto mínimo de JPA + H2).
 * Prueba directamente el repositorio Spring Data JPA que vive en
 * infraestructura.
 *
 * VALIDACIÓN CLAVE: al pedir PageRequest.of(0, 5) sobre 10 registros,
 * H2 devuelve exactamente 5 elementos y el total es 10.
 */
@DataJpaTest
@DisplayName("CuentaAhorro JPA Repository - Pruebas de paginación y persistencia")
class CuentaAhorroJpaRepositoryTest {

    @Autowired
    private CuentaAhorroJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        for (int i = 1; i <= 10; i++) {
            repository.save(CuentaAhorroEntity.builder()
                    .numeroCuenta("001-AHO-" + String.format("%03d", i))
                    .titular("Cliente Ahorro " + i)
                    .saldo(new BigDecimal(i * 1000))
                    .tasaInteresAnual(new BigDecimal("4.25"))
                    .fechaApertura(LocalDate.of(2024, 1, i))
                    .build());
        }
    }

    @Test
    @DisplayName("Debe haber exactamente 5 registros en la primera página de 10 totales")
    void testPaginacion_primeraPageDe5_sobre10Registros() {
        // ARRANGE: 10 registros ya insertados en setUp()
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("titular"));

        // ACT
        Page<CuentaAhorroEntity> page = repository.findAll(pageRequest);

        // ASSERT
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    @DisplayName("La segunda página debe devolver los 5 registros restantes")
    void testPaginacion_segundaPagina() {
        Page<CuentaAhorroEntity> page = repository.findAll(PageRequest.of(1, 5));

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.isLast()).isTrue();
        assertThat(page.hasPrevious()).isTrue();
    }

    @Test
    @DisplayName("Guardar y recuperar una CuentaAhorro conserva todos los campos")
    void testSave_conservaTodosLosCampos() {
        CuentaAhorroEntity entity = CuentaAhorroEntity.builder()
                .numeroCuenta("999-AHO-TEST")
                .titular("María Martínez")
                .saldo(new BigDecimal("15000.50"))
                .tasaInteresAnual(new BigDecimal("3.75"))
                .fechaApertura(LocalDate.of(2025, 6, 15))
                .build();

        CuentaAhorroEntity guardada = repository.save(entity);

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getNumeroCuenta()).isEqualTo("999-AHO-TEST");
        assertThat(guardada.getSaldo()).isEqualByComparingTo("15000.50");
        assertThat(guardada.getTasaInteresAnual()).isEqualByComparingTo("3.75");
    }
}
