package org.zgo.cuentacorriente.infrastructure.persistence.mapper;

import org.zgo.cuentacorriente.domain.model.CuentaCorriente;
import org.zgo.cuentacorriente.infrastructure.persistence.entity.CuentaCorrienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/* 
 * Mapper para convertir entre el modelo de dominio y la entidad JPA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CuentaCorrientePersistenceMapper {

    CuentaCorrienteEntity toEntity(CuentaCorriente domain);

    CuentaCorriente toDomain(CuentaCorrienteEntity entity);
}
