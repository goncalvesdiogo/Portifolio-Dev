package com.portifolio.apibff.usecases.impl;


import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.repositories.dtos.BrokerResponse;
import com.portifolio.apibff.repositories.mappers.BrokerRepositoryToEntityMapper;
import com.portifolio.apibff.usecases.RetrieveBrokersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetrieveBrokersUseCaseImpl implements RetrieveBrokersUseCase {

    private final BrokerRepositoryToEntityMapper mapper;
    private final DiscoveryClient discoveryClient;


    @Override
    public List<BrokerEntity> getAll() {
        var serviceInstance = discoveryClient.getInstances("servicestock").getFirst();
        var result = RestClient.builder().build().get()
                .uri(serviceInstance.getUri() + "/service-stock/brokers")
                .retrieve()
                .body(new ParameterizedTypeReference<List<BrokerResponse>>() {});
        return result.stream().map(mapper::from).toList();
    }

    @Override
    public BrokerEntity findById(Integer id) {
        var serviceInstance = discoveryClient.getInstances("servicestock").getFirst();
        var result = RestClient.builder().build().get()
                .uri(serviceInstance.getUri() + "/service-stock/brokers/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<BrokerResponse>() {});
        return mapper.from(result);
    }

}
