package org.zgo.cuentaahorro.infrastructure.web.controller;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.domain.model.SaldoCorrienteInfo;
import org.zgo.cuentaahorro.domain.port.in.CuentaAhorroUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/* 
 * Controlador REST para Cuenta de Ahorro.
 */
@RestController
@RequestMapping("/api/ahorro")
@RequiredArgsConstructor
public class CuentaAhorroController {

    private final CuentaAhorroUseCase cuentaAhorroUseCase;

    /**
     * Lista cuentas de ahorro con paginación.
     */
    @GetMapping
    public Page<CuentaAhorroResponse> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cuentaAhorroUseCase.listarCuentas(pageable);
    }

    /**
     * Crea una nueva cuenta de ahorro.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaAhorroResponse crear(@RequestBody CuentaAhorroRequest request) {
        return cuentaAhorroUseCase.crearCuenta(request);
    }

    /**
     * Obtiene una cuenta por su ID (consumido por cuenta-corriente-service).
     */
    @GetMapping("/{id}")
    public CuentaAhorroResponse obtener(@PathVariable Long id) {
        return cuentaAhorroUseCase.obtenerCuenta(id);
    }

    /**
     * Consulta el saldo de la cuenta corriente correspondiente.
     */
    @GetMapping("/{id}/saldo-corriente")
    public SaldoCorrienteInfo consultarSaldoCorriente(@PathVariable Long id) {
        return cuentaAhorroUseCase.consultarSaldoCorriente(id);
    }


}
