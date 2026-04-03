package org.zgo.cuentacorriente.infrastructure.web.controller;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.domain.model.AhorroInfo;
import org.zgo.cuentacorriente.domain.port.in.CuentaCorrienteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Adaptador de entrada REST para Cuenta Corriente.
 * Solo depende del puerto de entrada {@link CuentaCorrienteUseCase}.
 */
@RestController
@RequestMapping("/api/corriente")
@RequiredArgsConstructor
public class CuentaCorrienteController {

    private final CuentaCorrienteUseCase cuentaCorrienteUseCase;

    /**
     * GET /api/corriente?page=0&size=10
     */
    @GetMapping
    public Page<CuentaCorrienteResponse> listar(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return cuentaCorrienteUseCase.listarCuentas(pageable);
    }

    /**
     * POST /api/corriente — Devuelve 201 Created
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaCorrienteResponse crear(@RequestBody CuentaCorrienteRequest request) {
        return cuentaCorrienteUseCase.crearCuenta(request);
    }

    /**
     * GET /api/corriente/{id}
     * Obtiene una cuenta por su ID (consumido por cuenta-ahorro-service)
     */
    @GetMapping("/{id}")
    public CuentaCorrienteResponse obtener(@PathVariable Long id) {
        return cuentaCorrienteUseCase.obtenerCuenta(id);
    }

    /**
     * GET /api/corriente/{id}/ahorro-info
     * Consulta la cuenta de ahorro relacionada en cuenta-ahorro-service (sincrónico
     * vía RestClient).
     */
    @GetMapping("/{id}/ahorro-info")
    public AhorroInfo obtenerInfoAhorro(@PathVariable Long id) {
        return cuentaCorrienteUseCase.obtenerInfoAhorro(id);
    }
}
