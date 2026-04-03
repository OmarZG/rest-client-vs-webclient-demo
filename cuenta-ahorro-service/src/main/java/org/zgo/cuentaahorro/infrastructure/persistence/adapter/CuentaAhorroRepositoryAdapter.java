package org.zgo.cuentaahorro.infrastructure.persistence.adapter;

import org.zgo.cuentaahorro.domain.model.CuentaAhorro;
import org.zgo.cuentaahorro.domain.port.out.CuentaAhorroRepository;
import org.zgo.cuentaahorro.infrastructure.persistence.mapper.CuentaAhorroPersistenceMapper;
import org.zgo.cuentaahorro.infrastructure.persistence.spring.CuentaAhorroJpaRepository;
import org.zgo.cuentaahorro.infrastructure.persistence.entity.CuentaAhorroEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/* 
 * Adaptador de persistencia que implementa el puerto del dominio.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CuentaAhorroRepositoryAdapter implements CuentaAhorroRepository {

    private final CuentaAhorroJpaRepository jpaRepository;
    private final CuentaAhorroPersistenceMapper persistenceMapper;

    @Override
    public Page<CuentaAhorro> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(persistenceMapper::toDomain);
    }

    @Override
    public CuentaAhorro save(CuentaAhorro cuentaAhorro) {
        CuentaAhorroEntity entity = persistenceMapper.toEntity(cuentaAhorro);
        CuentaAhorroEntity saved = jpaRepository.save(entity);
        return persistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<CuentaAhorro> findById(Long id) {
        return jpaRepository.findById(id)
                .map(persistenceMapper::toDomain);
    }
}
