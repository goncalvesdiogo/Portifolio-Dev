package com.portifolio.servicestock.interactors;

import com.portifolio.servicestock.entities.Broker;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerRepositoryMapper {
    List<Broker> from (List<com.portifolio.servicestock.repositories.entities.Broker> brokerList);
}
