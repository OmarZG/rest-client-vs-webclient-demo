package org.zgo.cuentaahorro.infrastructure.client.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;

class MicroBClientAdapterTest {

    private MockWebServer mockWebServer;
    private MicroBClientAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        adapter = new MicroBClientAdapter(webClient);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void consultarSaldo_devuelveDatosCorrectos() throws Exception {

        String json = """
                    {
                      "id": 1,
                      "numeroCuenta": "001-CTE",
                      "titular": "Ana López",
                      "saldo": 8000.00,
                      "limiteCredito": 10000.00,
                      "banco": "BBVA"
                    }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        SaldoCorrienteInfo result = adapter.consultarSaldo(1L);

        assertThat(result).isNotNull();
        assertThat(result.titular()).isEqualTo("Ana López");
        assertThat(result.saldo()).isEqualByComparingTo("8000.00");
    }

    @Test
    void consultarSaldo_lanzaExcepcionCuandoFallaServicio() {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        assertThrows(IllegalStateException.class, () -> {
            adapter.consultarSaldo(99L);
        });
    }
}