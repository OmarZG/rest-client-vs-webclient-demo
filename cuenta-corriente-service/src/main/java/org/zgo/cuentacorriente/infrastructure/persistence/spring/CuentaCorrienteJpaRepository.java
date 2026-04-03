package org.zgo.cuentacorriente.infrastructure.persistence.spring;

import org.zgo.cuentacorriente.infrastructure.persistence.entity.CuentaCorrienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/* 
 * Repositorio Spring Data JPA para Cuenta Corriente.
 */
public interface CuentaCorrienteJpaRepository extends JpaRepository<CuentaCorrienteEntity, Long> {
}
