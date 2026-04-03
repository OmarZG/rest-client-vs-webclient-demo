package org.zgo.cuentaahorro.application.mapper;

import org.zgo.cuentaahorro.application.dto.CuentaAhorroRequest;
import org.zgo.cuentaahorro.application.dto.CuentaAhorroResponse;
import org.zgo.cuentaahorro.domain.model.CuentaAhorro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de capa de aplicación.
 * Responsabilidad: convertir entre DTOs (Request/Response) y el modelo de dominio.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CuentaAhorroMapper {

    /**
     * Convierte el DTO de entrada al modelo de dominio.
     * El id es ignorado porque en creación lo asigna la BD.
     */
    @Mapping(target = "id", ignore = true)
    CuentaAhorro toDomain(CuentaAhorroRequest request);

    /**
     * Convierte el modelo de dominio al DTO de salida para exponer en REST.
     */
    CuentaAhorroResponse toResponse(CuentaAhorro domain);
}
