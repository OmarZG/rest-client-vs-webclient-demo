package org.zgo.cuentaahorro.infrastructure.web.controller;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.zgo.cuentaahorro.domain.port.in.CuentaAhorroUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Test de contrato del controlador REST usando @WebMvcTest.
 * Solo se carga el contexto web (no JPA ni beans de servicio reales).
 *
 * @AutoConfigureWebTestClient permite usar WebTestClient de forma fluida
 *                             para validar status codes, JSON y manejo de
 *                             errores del flujo reactivo.
 */
@WebMvcTest(CuentaAhorroController.class)
@AutoConfigureWebTestClient
@DisplayName("CuentaAhorroController - Contratos REST y códigos de estado")
class CuentaAhorroControllerTest {

        @Autowired
        private WebTestClient webTestClient;

        @MockBean
        private CuentaAhorroUseCase cuentaAhorroUseCase; // Mockea el PUERTO, no la implementación

        @Autowired
        private ObjectMapper objectMapper;

        // ── GET /api/ahorro ────────────────────────────────────────────────────

        @Test
        @DisplayName("GET /api/ahorro: debe devolver 200 y la estructura de Page correcta")
        void listar_devuelve200ConPaginaValida() {
                CuentaAhorroResponse response = buildResponse(1L);
                when(cuentaAhorroUseCase.listarCuentas(any()))
                                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

                webTestClient.get()
                                .uri("/api/ahorro?page=0&size=10")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.content[0].id").isEqualTo(1)
                                .jsonPath("$.content[0].titular").isEqualTo("Ana López")
                                .jsonPath("$.content[0].tasaInteresAnual").isEqualTo(4.25)
                                .jsonPath("$.totalElements").isEqualTo(1);
        }

        // ── POST /api/ahorro ───────────────────────────────────────────────────

        @Test
        @DisplayName("POST /api/ahorro: debe devolver 201 y el JSON del recurso creado")
        void crear_devuelve201ConJson() throws Exception {
                CuentaAhorroRequest request = new CuentaAhorroRequest(
                                "001-AHO-001", "Ana López",
                                new BigDecimal("5000.00"), new BigDecimal("4.25"), LocalDate.now());
                CuentaAhorroResponse response = buildResponse(1L);

                when(cuentaAhorroUseCase.crearCuenta(any())).thenReturn(response);

                webTestClient.post()
                                .uri("/api/ahorro")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(objectMapper.writeValueAsString(request))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody()
                                .jsonPath("$.id").isEqualTo(1)
                                .jsonPath("$.numeroCuenta").isEqualTo("001-AHO-001");
        }

        // ── GET /api/ahorro/{id}/saldo-corriente ───────────────────────────────

        @Test
        @DisplayName("GET saldo-corriente: debe devolver 200 con datos del servicio externo")
        void consultarSaldoCorriente_devuelve200() {

                SaldoCorrienteInfo info = new SaldoCorrienteInfo(
                                1L, "001-CTE", "Ana López",
                                new BigDecimal("8000.00"), new BigDecimal("10000.00"), "BBVA");

                when(cuentaAhorroUseCase.consultarSaldoCorriente(1L))
                                .thenReturn(info); // 🔥 cambio aquí

                webTestClient.get()
                                .uri("/api/ahorro/1/saldo-corriente")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody()
                                .jsonPath("$.titular").isEqualTo("Ana López")
                                .jsonPath("$.banco").isEqualTo("BBVA");
        }

        @Test
        @DisplayName("GET saldo-corriente: error 500 cuando el servicio externo falla (manejo de timeout/error)")
        void consultarSaldoCorriente_error500CuandoServicioFalla() {

                when(cuentaAhorroUseCase.consultarSaldoCorriente(99L))
                                .thenThrow(new IllegalStateException("Servicio cuenta-corriente no disponible"));

                webTestClient.get()
                                .uri("/api/ahorro/99/saldo-corriente")
                                .exchange()
                                .expectStatus().is5xxServerError();
        }

        // ── helpers ────────────────────────────────────────────────────────────

        private CuentaAhorroResponse buildResponse(Long id) {
                return new CuentaAhorroResponse(id, "001-AHO-001", "Ana López",
                                new BigDecimal("5000.00"), new BigDecimal("4.25"), LocalDate.now());
        }
}
