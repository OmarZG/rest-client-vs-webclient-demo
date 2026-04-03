package org.zgo.cuentacorriente.infrastructure.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/* 
 * Configuración del RestClient para comunicación con cuenta-ahorro-service.
 */
@Configuration
public class RestClientConfig {

    /**
     * Bean del RestClient configurado para llamar a cuenta-ahorro-service.
     */
    @Bean
    public RestClient microARestClient(
            RestClient.Builder builder,
            @Value("${clients.micro-a.base-url}") String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-Origen", "cuenta-corriente-service")
                .build();
    }
}
