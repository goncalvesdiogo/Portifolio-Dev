package com.portifolio.apibff.usecases.impl;

import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.repositories.http.ServiceStocksBrokersFeignClient;
import com.portifolio.apibff.repositories.mappers.BrokerRepositoryToEntityMapper;
import com.portifolio.apibff.usecases.RetrieveBrokersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetrieveBrokersUseCaseImpl implements RetrieveBrokersUseCase {

    private final ServiceStocksBrokersFeignClient feignClient;
    private final BrokerRepositoryToEntityMapper mapper;


    @Override
    public List<BrokerEntity> getAll() {
        var result = feignClient.getAll();
        return result.getBody().stream().map(mapper::from).collect(Collectors.toList());


    }

    @Override
    public BrokerEntity findById(Integer id) {
        var result = feignClient.getById(id);
        return mapper.from(result.getBody());
    }


}
