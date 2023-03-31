package com.portifolio.servicestock.interactors.brokers.impl;

import com.portifolio.servicestock.entities.BrokerEntity;
import com.portifolio.servicestock.exceptions.ResourceNotFoundException;
import com.portifolio.servicestock.interactors.brokers.BrokerRepositoryMapper;
import com.portifolio.servicestock.interactors.brokers.BrokerUseCase;
import com.portifolio.servicestock.repositories.jpa.BrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@RequiredArgsConstructor
@Component
public class BrokerUseCaseImpl implements BrokerUseCase {

    private final BrokerRepository repository;
    private final BrokerRepositoryMapper mapper;

    @Override
    public List<BrokerEntity> findAll() {
        return mapper.fromList(repository.findAll());
    }

    @Override
    public BrokerEntity findById(Integer id) {
        var result = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Broker not found by Id:" + id));
        return mapper.from(result);
    }

    @Override
    public BrokerEntity create(BrokerEntity entity) {
        entity.setId(null);
        var result = repository.save(mapper.from(entity));
        return mapper.from(result);
    }

    @Override
    public BrokerEntity update(BrokerEntity newEntity) {
        var oldEntity = findById(newEntity.getId());
        copyProperties(newEntity, oldEntity);
        var result = repository.save(mapper.from(oldEntity));
        return mapper.from(result);
    }

    @Override
    public void remove(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

}
