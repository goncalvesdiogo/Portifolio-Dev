package com.portifolio.apibff.usecases.impl;


import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.repositories.dtos.BrokerResponse;
import com.portifolio.apibff.repositories.mappers.BrokerRepositoryToEntityMapper;
import com.portifolio.apibff.usecases.RetrieveBrokersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetrieveBrokersUseCaseImpl implements RetrieveBrokersUseCase {

    private final BrokerRepositoryToEntityMapper mapper;
    private final DiscoveryClient discoveryClient;
    private ServiceInstance serviceInstance = discoveryClient.getInstances("servicestock").get(0);

    @Override
    public List<BrokerEntity> getAll() {
        var result = RestClient.builder().build().get()
                .uri(serviceInstance.getUri() + "/service-stock/brokers")
                .retrieve()
                .body(new ParameterizedTypeReference<List<BrokerResponse>>() {});
        return result.stream().map(mapper::from).collect(Collectors.toList());
    }

    @Override
    public BrokerEntity findById(Integer id) {
        var result = RestClient.builder().build().get()
                .uri(serviceInstance.getUri() + "/service-stock/brokers/{id}", id)
                .retrieve()
                .body(new ParameterizedTypeReference<BrokerResponse>() {});
        return mapper.from(result);
    }

}
