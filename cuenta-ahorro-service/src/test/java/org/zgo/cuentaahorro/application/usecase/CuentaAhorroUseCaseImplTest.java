package org.zgo.cuentaahorro.application.usecase;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.application.mapper.CuentaAhorroMapper;
import org.zgo.cuentaahorro.domain.model.CuentaAhorro;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.zgo.cuentaahorro.domain.port.out.CuentaAhorroRepository;
import org.zgo.cuentaahorro.domain.port.out.SaldoCorrientePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario puro del caso de uso. Usa Mockito para aislar completamente el
 * dominio.
 *
 * PRINCIPIO CLAVE: Los mocks son de tipo INTERFAZ (puerto de dominio),
 * nunca de clase de infraestructura. Esto garantiza que el dominio
 * no tiene dependencias en la implementación concreta.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CuentaAhorroUseCaseImpl - Pruebas unitarias del caso de uso")
class CuentaAhorroUseCaseImplTest {

        @Mock
        private CuentaAhorroRepository cuentaAhorroRepository; // Puerto de dominio (interfaz)

        @Mock
        private SaldoCorrientePort saldoCorrientePort; // Puerto de dominio (interfaz)

        @Mock
        private CuentaAhorroMapper cuentaAhorroMapper;

        @InjectMocks
        private CuentaAhorroUseCaseImpl useCase;

        // ── listarCuentas ─────────────────────────────────────────────────────

        @Test
        @DisplayName("listarCuentas: debe delegar paginación al repositorio y retornar DTOs mapeados")
        void listarCuentas_delegaAlRepositorio() {
                CuentaAhorro domainObj = buildDomainCuenta(1L);
                CuentaAhorroResponse responseDto = buildResponse(1L);
                Page<CuentaAhorro> pageResult = new PageImpl<>(List.of(domainObj));

                when(cuentaAhorroRepository.findAll(any())).thenReturn(pageResult);
                when(cuentaAhorroMapper.toResponse(domainObj)).thenReturn(responseDto);

                Page<CuentaAhorroResponse> result = useCase.listarCuentas(PageRequest.of(0, 10));

                assertThat(result.getContent()).hasSize(1);
                assertThat(result.getContent().get(0).titular()).isEqualTo("Ana López");
                verify(cuentaAhorroRepository, times(1)).findAll(any());
        }

        // ── crearCuenta ───────────────────────────────────────────────────────

        @Test
        @DisplayName("crearCuenta: debe mapear request→dominio, persistir y retornar response")
        void crearCuenta_mapeoYPersistencia() {
                CuentaAhorroRequest request = new CuentaAhorroRequest(
                                "001-AHO-001", "Ana López",
                                new BigDecimal("5000.00"), new BigDecimal("4.25"), LocalDate.now());
                CuentaAhorro domainObj = buildDomainCuenta(null);
                CuentaAhorro domainGuardado = buildDomainCuenta(1L);
                CuentaAhorroResponse responseDto = buildResponse(1L);

                when(cuentaAhorroMapper.toDomain(request)).thenReturn(domainObj);
                when(cuentaAhorroRepository.save(domainObj)).thenReturn(domainGuardado);
                when(cuentaAhorroMapper.toResponse(domainGuardado)).thenReturn(responseDto);

                CuentaAhorroResponse result = useCase.crearCuenta(request);

                assertThat(result.id()).isEqualTo(1L);
                verify(cuentaAhorroRepository, times(1)).save(domainObj);
                verify(cuentaAhorroMapper, times(1)).toDomain(request);
        }

        // ── consultarSaldoCorriente ────────────────────────────────────────────

        @Test
        @DisplayName("consultarSaldoCorriente: debe delegar al puerto y retornar SaldoCorrienteInfo")
        void consultarSaldoCorriente_delegaAlPuerto() {

                SaldoCorrienteInfo infoEsperada = new SaldoCorrienteInfo(
                                1L, "001-CTE-001", "Ana López",
                                new BigDecimal("8000.00"), new BigDecimal("10000.00"), "BBVA");

                when(saldoCorrientePort.consultarSaldo(1L)).thenReturn(infoEsperada);

                SaldoCorrienteInfo resultado = useCase.consultarSaldoCorriente(1L);

                assertNotNull(resultado);
                assertEquals("Ana López", resultado.titular());
                assertEquals(0, resultado.saldo().compareTo(new BigDecimal("8000.00")));

                verify(saldoCorrientePort, times(1)).consultarSaldo(1L);
        }

        @Test
        @DisplayName("consultarSaldoCorriente: debe propagar el error del puerto sin modificarlo")
        void consultarSaldoCorriente_propagaError() {

                when(saldoCorrientePort.consultarSaldo(99L))
                                .thenThrow(new IllegalStateException("Servicio no disponible"));

                IllegalStateException ex = assertThrows(
                                IllegalStateException.class,
                                () -> useCase.consultarSaldoCorriente(99L));

                assertTrue(ex.getMessage().contains("Servicio no disponible"));
        }

        // ── helpers ───────────────────────────────────────────────────────────

        private CuentaAhorro buildDomainCuenta(Long id) {
                return CuentaAhorro.builder()
                                .id(id).numeroCuenta("001-AHO-001").titular("Ana López")
                                .saldo(new BigDecimal("5000.00"))
                                .tasaInteresAnual(new BigDecimal("4.25"))
                                .fechaApertura(LocalDate.now()).build();
        }

        private CuentaAhorroResponse buildResponse(Long id) {
                return new CuentaAhorroResponse(id, "001-AHO-001", "Ana López",
                                new BigDecimal("5000.00"), new BigDecimal("4.25"), LocalDate.now());
        }
}
