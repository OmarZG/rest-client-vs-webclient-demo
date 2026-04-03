package org.zgo.cuentaahorro.domain.port.in;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CuentaAhorroUseCase {

    Page<CuentaAhorroResponse> listarCuentas(Pageable pageable);

    CuentaAhorroResponse crearCuenta(CuentaAhorroRequest request);

    CuentaAhorroResponse obtenerCuenta(Long id);

    SaldoCorrienteInfo consultarSaldoCorriente(Long cuentaId);
}
