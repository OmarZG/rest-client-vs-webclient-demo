package org.zgo.cuentacorriente.application.usecase;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.application.mapper.CuentaCorrienteMapper;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.domain.model.CuentaCorriente;
import org.zgo.cuentacorriente.domain.port.in.CuentaCorrienteUseCase;
import org.zgo.cuentacorriente.domain.port.out.AhorroInfoPort;
import org.zgo.cuentacorriente.domain.port.out.CuentaCorrienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/* 
 * Implementación del caso de uso de Cuenta Corriente.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaCorrienteUseCaseImpl implements CuentaCorrienteUseCase {

    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final AhorroInfoPort ahorroInfoPort;
    private final CuentaCorrienteMapper cuentaCorrienteMapper;

    @Override
    public Page<CuentaCorrienteResponse> listarCuentas(Pageable pageable) {
        log.info("Listando cuentas corrientes con paginación: {}", pageable);
        return cuentaCorrienteRepository.findAll(pageable)
                .map(cuentaCorrienteMapper::toResponse);
    }

    @Override
    public CuentaCorrienteResponse crearCuenta(CuentaCorrienteRequest request) {
        log.info("Creando cuenta corriente para titular: {} en banco: {}", request.titular(), request.banco());
        CuentaCorriente nuevaCuenta = cuentaCorrienteMapper.toDomain(request);
        CuentaCorriente guardada = cuentaCorrienteRepository.save(nuevaCuenta);
        return cuentaCorrienteMapper.toResponse(guardada);
    }

    @Override
    public CuentaCorrienteResponse obtenerCuenta(Long id) {
        CuentaCorriente cuenta = cuentaCorrienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta corriente no encontrada: " + id));
        return cuentaCorrienteMapper.toResponse(cuenta);
    }

    @Override
    public AhorroInfo obtenerInfoAhorro(Long cuentaId) {
        log.info("Consultando info de ahorro en cuenta-ahorro-service para cuentaId: {}", cuentaId);
        return ahorroInfoPort.obtenerInfo(cuentaId);
    }
}
