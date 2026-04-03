package org.zgo.cuentacorriente.application.usecase;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.application.mapper.CuentaCorrienteMapper;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.domain.model.CuentaCorriente;
import org.zgo.cuentacorriente.domain.port.out.AhorroInfoPort;
import org.zgo.cuentacorriente.domain.port.out.CuentaCorrienteRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario puro: los mocks son interfaces de dominio (puertos de salida),
 * nunca implementaciones de infraestructura (JPA, RestClient, etc.).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CuentaCorrienteUseCaseImpl - Tests unitarios del caso de uso")
class CuentaCorrienteUseCaseImplTest {

    @Mock
    private CuentaCorrienteRepository cuentaCorrienteRepository;  // puerto de dominio

    @Mock
    private AhorroInfoPort ahorroInfoPort;                         // puerto de dominio

    @Mock
    private CuentaCorrienteMapper cuentaCorrienteMapper;

    @InjectMocks
    private CuentaCorrienteUseCaseImpl useCase;

    @Test
    @DisplayName("listarCuentas: delega al repositorio y aplica mapeo a DTO")
    void listarCuentas_delegaAlRepositorio() {
        CuentaCorriente domain = buildDomain(1L);
        CuentaCorrienteResponse dto = buildResponse(1L);
        when(cuentaCorrienteRepository.findAll(any())).thenReturn(new PageImpl<>(List.of(domain)));
        when(cuentaCorrienteMapper.toResponse(domain)).thenReturn(dto);

        Page<CuentaCorrienteResponse> result = useCase.listarCuentas(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).banco()).isEqualTo("BBVA");
        verify(cuentaCorrienteRepository).findAll(any());
    }

    @Test
    @DisplayName("crearCuenta: mapea request → dominio, persiste, retorna response")
    void crearCuenta_mapeoCompleto() {
        CuentaCorrienteRequest request = new CuentaCorrienteRequest(
                "001-CTE", "Corp S.A.", new BigDecimal("50000"),
                new BigDecimal("100000"), "BBVA", LocalDate.now());
        CuentaCorriente domain = buildDomain(null);
        CuentaCorriente guardado = buildDomain(1L);
        CuentaCorrienteResponse response = buildResponse(1L);

        when(cuentaCorrienteMapper.toDomain(request)).thenReturn(domain);
        when(cuentaCorrienteRepository.save(domain)).thenReturn(guardado);
        when(cuentaCorrienteMapper.toResponse(guardado)).thenReturn(response);

        CuentaCorrienteResponse result = useCase.crearCuenta(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.limiteCredito()).isEqualByComparingTo("100000.00");
        verify(cuentaCorrienteRepository).save(domain);
    }

    @Test
    @DisplayName("obtenerInfoAhorro: delega al puerto y retorna el AhorroInfo correcto")
    void obtenerInfoAhorro_delegaAlPuerto() {
        AhorroInfo esperado = new AhorroInfo(1L, "001-AHO", "Ana López",
                new BigDecimal("5000"), new BigDecimal("4.25"), LocalDate.now());
        when(ahorroInfoPort.obtenerInfo(1L)).thenReturn(esperado);

        AhorroInfo result = useCase.obtenerInfoAhorro(1L);

        assertThat(result.tasaInteresAnual()).isEqualByComparingTo("4.25");
        verify(ahorroInfoPort).obtenerInfo(1L);
    }

    @Test
    @DisplayName("obtenerInfoAhorro: propaga excepción del puerto sin modificarla")
    void obtenerInfoAhorro_propagaExcepcion() {
        when(ahorroInfoPort.obtenerInfo(99L))
                .thenThrow(new IllegalStateException("Servicio no disponible"));

        assertThatThrownBy(() -> useCase.obtenerInfoAhorro(99L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Servicio no disponible");
    }

    private CuentaCorriente buildDomain(Long id) {
        return CuentaCorriente.builder()
                .id(id).numeroCuenta("001-CTE").titular("Corp S.A.")
                .saldo(new BigDecimal("50000")).limiteCredito(new BigDecimal("100000.00"))
                .banco("BBVA").fechaApertura(LocalDate.now()).build();
    }

    private CuentaCorrienteResponse buildResponse(Long id) {
        return new CuentaCorrienteResponse(id, "001-CTE", "Corp S.A.",
                new BigDecimal("50000"), new BigDecimal("100000.00"),
                "BBVA", LocalDate.now());
    }
}
