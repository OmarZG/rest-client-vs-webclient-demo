package org.zgo.cuentacorriente.application.mapper;

import org.zgo.cuentacorriente.application.dto.CuentaCorrienteRequest;
import org.zgo.cuentacorriente.application.dto.CuentaCorrienteResponse;
import org.zgo.cuentacorriente.domain.model.CuentaCorriente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper de capa de aplicación.
 * Convierte entre DTOs (Request/Response) y el modelo de dominio.
 * No conoce entidades JPA — eso es responsabilidad del persistence mapper.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CuentaCorrienteMapper {

    @Mapping(target = "id", ignore = true)
    CuentaCorriente toDomain(CuentaCorrienteRequest request);

    CuentaCorrienteResponse toResponse(CuentaCorriente domain);
}
