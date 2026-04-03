package org.zgo.cuentaahorro.infrastructure.persistence.spring;

import org.zgo.cuentaahorro.infrastructure.persistence.entity.CuentaAhorroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/* 
 * Repositorio Spring Data JPA para Cuenta de Ahorro.
 */
public interface CuentaAhorroJpaRepository extends JpaRepository<CuentaAhorroEntity, Long> {
}
