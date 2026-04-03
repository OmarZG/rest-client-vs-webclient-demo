package org.zgo.cuentaahorro.infrastructure.client.adapter;

import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.zgo.cuentaahorro.domain.port.out.SaldoCorrientePort;
import org.zgo.cuentaahorro.infrastructure.client.dto.RemotaCuentaCorrienteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/* 
 * Adaptador de salida para comunicación con cuenta-corriente-service.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MicroBClientAdapter implements SaldoCorrientePort {

        private final WebClient microBWebClient;

        @Override
        public SaldoCorrienteInfo consultarSaldo(Long cuentaId) {
                log.info("Llamando a cuenta-corriente-service para obtener saldo de cuentaId={}", cuentaId);

                return microBWebClient.get()
                                .uri("/api/corriente/{id}", cuentaId)
                                .retrieve()
                                .bodyToMono(RemotaCuentaCorrienteDto.class)
                                .timeout(Duration.ofSeconds(5))
                                .map(dto -> new SaldoCorrienteInfo(
                                                dto.id(),
                                                dto.numeroCuenta(),
                                                dto.titular(),
                                                dto.saldo(),
                                                dto.limiteCredito(),
                                                dto.banco()))
                                .doOnError(ex -> log.error("Error al consultar cuenta-corriente-service para cuentaId={}: {}", cuentaId,
                                                ex.getMessage()))
                                .onErrorResume(ex -> Mono.error(
                                                new IllegalStateException("Servicio cuenta-corriente no disponible: "
                                                                + ex.getMessage(), ex)))
                                .block();
        }
}
