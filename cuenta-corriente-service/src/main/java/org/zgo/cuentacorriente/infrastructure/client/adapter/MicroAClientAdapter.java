package org.zgo.cuentacorriente.infrastructure.client.adapter;

import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.domain.port.out.AhorroInfoPort;
import org.zgo.cuentacorriente.infrastructure.client.dto.RemotaCuentaAhorroDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/* 
 * Adaptador de salida para comunicación con cuenta-ahorro-service usando RestClient.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MicroAClientAdapter implements AhorroInfoPort {

    private final RestClient microARestClient;

    @Override
    public AhorroInfo obtenerInfo(Long cuentaId) {
        log.info("Llamando a cuenta-ahorro-service para obtener info de ahorro, cuentaId={}", cuentaId);

        try {
            RemotaCuentaAhorroDto dto = microARestClient.get()
                    .uri("/api/ahorro/{id}", cuentaId)
                    .retrieve()
                    .body(RemotaCuentaAhorroDto.class);

            if (dto == null) {
                throw new IllegalStateException("cuenta-ahorro-service devolvió respuesta vacía para cuentaId=" + cuentaId);
            }

            // Mapeo del DTO al objeto de dominio
            return new AhorroInfo(
                    dto.id(),
                    dto.numeroCuenta(),
                    dto.titular(),
                    dto.saldo(),
                    dto.tasaInteresAnual(),
                    dto.fechaApertura()
            );

        } catch (HttpClientErrorException.NotFound ex) {
            log.warn("Cuenta de ahorro no encontrada en cuenta-ahorro-service para cuentaId={}", cuentaId);
            throw new IllegalArgumentException("Cuenta de ahorro no encontrada: " + cuentaId, ex);
        } catch (Exception ex) {
            log.error("Error al consultar cuenta-ahorro-service para cuentaId={}: {}", cuentaId, ex.getMessage());
            throw new IllegalStateException("Servicio cuenta-ahorro no disponible: " + ex.getMessage(), ex);
        }
    }
}
