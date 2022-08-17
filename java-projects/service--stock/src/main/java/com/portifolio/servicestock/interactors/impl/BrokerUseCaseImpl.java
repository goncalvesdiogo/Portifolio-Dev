package com.portifolio.servicestock.interactors.impl;

import com.portifolio.servicestock.entities.Broker;
import com.portifolio.servicestock.interactors.BrokerRepositoryMapper;
import com.portifolio.servicestock.interactors.BrokerUseCase;
import com.portifolio.servicestock.repositories.jpa.BrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BrokerUseCaseImpl implements BrokerUseCase {

    private final BrokerRepository repository;
    private final BrokerRepositoryMapper mapper;

    @Override
    public List<Broker> getAllList() {
        return mapper.from(repository.findAll());
    }
}
