package com.portifolio.apibff.repositories.mappers;

import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.repositories.dtos.BrokerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerRepositoryToEntityMapper {

    BrokerEntity from(BrokerResponse brokerResponse);
}
