package com.portifolio.servicestock.interactors.brokers;

import com.portifolio.servicestock.entities.BrokerEntity;
import com.portifolio.servicestock.repositories.entities.Broker;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerRepositoryMapper {
    List<BrokerEntity> fromList (List<Broker> brokerList);

    BrokerEntity from (Broker broker);

    Broker from (BrokerEntity brokerEntity);
}
