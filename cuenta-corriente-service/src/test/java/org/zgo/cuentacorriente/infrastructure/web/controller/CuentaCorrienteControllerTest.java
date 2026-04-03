package org.zgo.cuentacorriente.infrastructure.web.controller;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.domain.port.in.CuentaCorrienteUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @WebMvcTest: contexto web mínimo solo con el controlador especificado.
 * Valida que MapStruct entrega el JSON correcto y que los códigos 200/201/500
 * se respetan según el comportamiento del Use Case mockeado.
 */
@WebMvcTest(CuentaCorrienteController.class)
@DisplayName("CuentaCorrienteController - Contratos JSON y códigos de estado")
class CuentaCorrienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaCorrienteUseCase cuentaCorrienteUseCase;  // puerto de entrada

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ── GET /api/corriente ────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/corriente: debe devolver 200 con la estructura de Page y campos correctos")
    void listar_devuelve200ConPaginaYCamposCorrectos() throws Exception {
        CuentaCorrienteResponse response = buildResponse(1L);
        when(cuentaCorrienteUseCase.listarCuentas(any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/corriente?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titular").value("Corp S.A."))
                .andExpect(jsonPath("$.content[0].banco").value("BBVA"))
                .andExpect(jsonPath("$.content[0].limiteCredito").value(100000.00))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // ── POST /api/corriente ───────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/corriente: debe devolver 201 Created con JSON del recurso")
    void crear_devuelve201ConJsonCorrecto() throws Exception {
        CuentaCorrienteRequest request = new CuentaCorrienteRequest(
                "001-CTE-001", "Corp S.A.",
                new BigDecimal("50000.00"), new BigDecimal("100000.00"),
                "BBVA", LocalDate.of(2025, 1, 1));
        CuentaCorrienteResponse response = buildResponse(1L);
        when(cuentaCorrienteUseCase.crearCuenta(any())).thenReturn(response);

        mockMvc.perform(post("/api/corriente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("001-CTE-001"))
                .andExpect(jsonPath("$.banco").value("BBVA"));
    }

    // ── GET /api/corriente/{id}/ahorro-info ──────────────────────────────

    @Test
    @DisplayName("GET ahorro-info: debe devolver 200 con datos del servicio externo Micro-A")
    void obtenerInfoAhorro_devuelve200ConDatosMicroA() throws Exception {
        AhorroInfo info = new AhorroInfo(1L, "001-AHO", "Ana López",
                new BigDecimal("5000.00"), new BigDecimal("4.25"),
                LocalDate.of(2024, 3, 15));
        when(cuentaCorrienteUseCase.obtenerInfoAhorro(1L)).thenReturn(info);

        mockMvc.perform(get("/api/corriente/1/ahorro-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titular").value("Ana López"))
                .andExpect(jsonPath("$.tasaInteresAnual").value(4.25));
    }

    @Test
    @DisplayName("GET ahorro-info: debe responder 500 cuando Micro-A no está disponible")
    void obtenerInfoAhorro_devuelve500CuandoServicioFalla() throws Exception {
        when(cuentaCorrienteUseCase.obtenerInfoAhorro(99L))
                .thenThrow(new IllegalStateException("Servicio cuenta-ahorro no disponible"));

        mockMvc.perform(get("/api/corriente/99/ahorro-info"))
                .andExpect(status().isInternalServerError());
    }

    private CuentaCorrienteResponse buildResponse(Long id) {
        return new CuentaCorrienteResponse(id, "001-CTE-001", "Corp S.A.",
                new BigDecimal("50000.00"), new BigDecimal("100000.00"),
                "BBVA", LocalDate.of(2025, 1, 1));
    }
}
