package org.zgo.cuentacorriente.infrastructure.persistence.adapter;

import org.zgo.cuentacorriente.domain.model.CuentaCorriente;
import org.zgo.cuentacorriente.domain.port.out.CuentaCorrienteRepository;
import org.zgo.cuentacorriente.infrastructure.persistence.mapper.CuentaCorrientePersistenceMapper;
import org.zgo.cuentacorriente.infrastructure.persistence.spring.CuentaCorrienteJpaRepository;
import org.zgo.cuentacorriente.infrastructure.persistence.entity.CuentaCorrienteEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/* 
 * Adaptador de persistencia para Cuenta Corriente.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CuentaCorrienteRepositoryAdapter implements CuentaCorrienteRepository {

    private final CuentaCorrienteJpaRepository jpaRepository;
    private final CuentaCorrientePersistenceMapper persistenceMapper;

    @Override
    public Page<CuentaCorriente> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(persistenceMapper::toDomain);
    }

    @Override
    public CuentaCorriente save(CuentaCorriente cuenta) {
        CuentaCorrienteEntity entity = persistenceMapper.toEntity(cuenta);
        return persistenceMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<CuentaCorriente> findById(Long id) {
        return jpaRepository.findById(id).map(persistenceMapper::toDomain);
    }
}
