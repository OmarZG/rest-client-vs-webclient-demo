package org.zgo.cuentacorriente.infrastructure.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.infrastructure.client.adapter.MicroAClientAdapter;
import org.zgo.cuentacorriente.infrastructure.client.config.RestClientConfig;
import org.zgo.cuentacorriente.infrastructure.client.dto.RemotaCuentaAhorroDto;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestClientTest(MicroAClientAdapter.class)
@Import(RestClientConfig.class)
@ActiveProfiles("test")
class MicroAClientTest {

        @Autowired
        private MicroAClientAdapter microAClientAdapter;

        @Autowired
        private MockRestServiceServer server;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void obtenerInfo_devuelveDatosCorrectos() throws Exception {

                RemotaCuentaAhorroDto mockResponse = new RemotaCuentaAhorroDto(
                                1L,
                                "001-AHO-001",
                                "Ana López",
                                new BigDecimal("150.00"),
                                new BigDecimal("4.25"),
                                LocalDate.now());

                server.expect(requestTo("http://localhost:8081/api/ahorro/1"))
                                .andRespond(withSuccess(
                                                objectMapper.writeValueAsString(mockResponse),
                                                MediaType.APPLICATION_JSON));

                AhorroInfo result = microAClientAdapter.obtenerInfo(1L);

                assertThat(result).isNotNull();
                assertThat(result.titular()).isEqualTo("Ana López");
                assertThat(result.saldo()).isEqualByComparingTo("150.00");
        }
}