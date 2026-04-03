package org.zgo.cuentaahorro.infrastructure.persistence.mapper;

import org.zgo.cuentaahorro.domain.model.CuentaAhorro;
import org.zgo.cuentaahorro.infrastructure.persistence.entity.CuentaAhorroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/* 
 * Mapper para convertir entre el modelo de dominio y la entidad JPA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CuentaAhorroPersistenceMapper {

    /** Convierte modelo de dominio → entidad JPA para persistir */
    CuentaAhorroEntity toEntity(CuentaAhorro domain);

    /** Convierte entidad JPA → modelo de dominio después de leer de BD */
    CuentaAhorro toDomain(CuentaAhorroEntity entity);
}
