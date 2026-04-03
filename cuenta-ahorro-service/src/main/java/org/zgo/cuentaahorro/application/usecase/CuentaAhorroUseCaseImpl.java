package org.zgo.cuentaahorro.application.usecase;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.application.mapper.CuentaAhorroMapper;
import org.zgo.cuentaahorro.domain.model.CuentaAhorro;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.zgo.cuentaahorro.domain.port.in.CuentaAhorroUseCase;
import org.zgo.cuentaahorro.domain.port.out.CuentaAhorroRepository;
import org.zgo.cuentaahorro.domain.port.out.SaldoCorrientePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/*
 * Implementación del caso de uso (orquestador del dominio).
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaAhorroUseCaseImpl implements CuentaAhorroUseCase {

    // Puertos de salida — interfaces del dominio, no implementaciones de infra
    private final CuentaAhorroRepository cuentaAhorroRepository;
    private final SaldoCorrientePort saldoCorrientePort;
    private final CuentaAhorroMapper cuentaAhorroMapper;

    @Override
    public Page<CuentaAhorroResponse> listarCuentas(Pageable pageable) {
        log.info("Listando cuentas de ahorro con paginación: {}", pageable);
        return cuentaAhorroRepository.findAll(pageable)
                .map(cuentaAhorroMapper::toResponse);
    }

    @Override
    public CuentaAhorroResponse crearCuenta(CuentaAhorroRequest request) {
        log.info("Creando cuenta de ahorro titular: {}", request.titular());
        CuentaAhorro nuevaCuenta = cuentaAhorroMapper.toDomain(request);
        CuentaAhorro guardada = cuentaAhorroRepository.save(nuevaCuenta);
        return cuentaAhorroMapper.toResponse(guardada);
    }

    @Override
    public CuentaAhorroResponse obtenerCuenta(Long id) {
        CuentaAhorro cuenta = cuentaAhorroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta de ahorro no encontrada: " + id));
        return cuentaAhorroMapper.toResponse(cuenta);
    }

    @Override
    public SaldoCorrienteInfo consultarSaldoCorriente(Long cuentaId) {
        log.info("Consultando saldo corriente en Micro-B para cuentaId: {}", cuentaId);
        return saldoCorrientePort.consultarSaldo(cuentaId);
    }
}
