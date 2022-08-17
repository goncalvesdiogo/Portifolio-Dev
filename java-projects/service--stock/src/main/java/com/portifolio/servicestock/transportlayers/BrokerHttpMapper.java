package com.portifolio.servicestock.transportlayers;


import com.portifolio.servicestock.entities.Broker;
import com.portifolio.servicestock.transportlayers.dtos.BrokerDAO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerHttpMapper {

    List<BrokerDAO> map(List<Broker> brokerList);


}
