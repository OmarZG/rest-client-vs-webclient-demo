package org.zgo.cuentacorriente.infrastructure.client.adapter;

import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.infrastructure.client.config.RestClientConfig;
import org.zgo.cuentacorriente.infrastructure.client.dto.RemotaCuentaAhorroDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * @RestClientTest: Carga un contexto mínimo con MockRestServiceServer.
 * Las llamadas HTTP del RestClient son interceptadas por el mock server.
 *
 * @Import(RestClientConfig.class): Necesario para que el @Bean microARestClient
 *   sea creado usando el RestClient.Builder auto-configurado por @RestClientTest
 *   (que está conectado al MockRestServiceServer).
 *
 * @TestPropertySource: Provee la propiedad que RestClientConfig necesita para
 *   construir el bean (sin esta propiedad, falla el contexto de prueba).
 *
 * VALIDACIÓN CLAVE: El RestClient sabe deserializar el JSON de Micro-A
 * correctamente, incluyendo LocalDate (requiere JavaTimeModule en Jackson).
 */
@RestClientTest(MicroAClientAdapter.class)
@Import(RestClientConfig.class)
@TestPropertySource(properties = "clients.micro-a.base-url=http://localhost:8081")
@DisplayName("MicroAClientAdapter - Pruebas de deserialización con MockRestServiceServer")
class MicroAClientAdapterTest {

    @Autowired
    private MicroAClientAdapter microAClientAdapter;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe deserializar correctamente el JSON de Micro-A a AhorroInfo de dominio")
    void obtenerInfo_deserializaJsonCorrectamente() throws Exception {
        // Configurar Jackson para LocalDate si no está ya configurado en el contexto del test
        objectMapper.registerModule(new JavaTimeModule());

        RemotaCuentaAhorroDto mockDto = new RemotaCuentaAhorroDto(
                1L, "001-AHO-001", "Ana López",
                new BigDecimal("5000.00"), new BigDecimal("4.25"),
                LocalDate.of(2024, 3, 15));

        // Configurar el servidor mock para responder al GET /api/ahorro/1
        server.expect(requestTo("http://localhost:8081/api/ahorro/1"))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(mockDto),
                        MediaType.APPLICATION_JSON));

        // ACT
        AhorroInfo result = microAClientAdapter.obtenerInfo(1L);

        // ASSERT: El adaptador mapeó correctamente DTO → Value Object de dominio
        assertThat(result).isNotNull();
        assertThat(result.titular()).isEqualTo("Ana López");
        assertThat(result.saldo()).isEqualByComparingTo("5000.00");
        assertThat(result.tasaInteresAnual()).isEqualByComparingTo("4.25");
        assertThat(result.fechaApertura()).isEqualTo(LocalDate.of(2024, 3, 15));

        server.verify();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando Micro-A responde 404")
    void obtenerInfo_lanzaExcepcion_cuando404() {
        server.expect(requestTo("http://localhost:8081/api/ahorro/999"))
                .andRespond(withResourceNotFound());

        assertThatThrownBy(() -> microAClientAdapter.obtenerInfo(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("999");

        server.verify();
    }

    @Test
    @DisplayName("Debe lanzar IllegalStateException cuando Micro-A responde 500")
    void obtenerInfo_lanzaExcepcion_cuando500() {
        server.expect(requestTo("http://localhost:8081/api/ahorro/1"))
                .andRespond(withServerError());

        assertThatThrownBy(() -> microAClientAdapter.obtenerInfo(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Servicio cuenta-ahorro no disponible");

        server.verify();
    }
}
