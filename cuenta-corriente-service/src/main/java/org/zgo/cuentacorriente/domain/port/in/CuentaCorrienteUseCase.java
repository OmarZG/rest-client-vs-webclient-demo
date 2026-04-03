package org.zgo.cuentacorriente.domain.port.in;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Puerto de ENTRADA para el bounded context de Cuenta Corriente.
 * El controlador (adaptador primario) lo usa para comunicarse con el dominio.
 */
public interface CuentaCorrienteUseCase {

    Page<CuentaCorrienteResponse> listarCuentas(Pageable pageable);

    CuentaCorrienteResponse crearCuenta(CuentaCorrienteRequest request);

    CuentaCorrienteResponse obtenerCuenta(Long id);

    /**
     * Obtiene información de la cuenta de ahorro relacionada en Micro-A
     * de forma sincrónica usando RestClient.
     */
    AhorroInfo obtenerInfoAhorro(Long cuentaId);
}
