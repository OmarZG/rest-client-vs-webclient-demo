package org.zgo.cuentaahorro.infrastructure.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/* 
 * Configuración centralizada del WebClient para comunicación con cuenta-corriente-service.
 */
@Configuration
public class WebClientConfig {

    /**
     * Bean del WebClient configurado para llamar a cuenta-corriente-service.
     */
    @Bean
    public WebClient microBWebClient(
            WebClient.Builder builder,
            @Value("${clients.micro-b.base-url}") String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-Origen", "cuenta-ahorro-service")
                .build();
    }
}
