package com.portifolio.servicestock.transportlayers.brokers;


import com.portifolio.servicestock.entities.BrokerEntity;
import com.portifolio.servicestock.transportlayers.brokers.dtos.BrokerPostRequest;
import com.portifolio.servicestock.transportlayers.brokers.dtos.BrokerResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerHttpMapper {

    List<BrokerResponse> fromList(List<BrokerEntity> entityList);

    BrokerResponse from(BrokerEntity entity);

    BrokerEntity from(BrokerPostRequest request);


}
