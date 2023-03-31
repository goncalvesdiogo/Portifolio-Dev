package com.portifolio.apibff.transport.mappers;

import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.model.Broker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerEntityToDtoMapper {


    Broker from(BrokerEntity entity);
}
