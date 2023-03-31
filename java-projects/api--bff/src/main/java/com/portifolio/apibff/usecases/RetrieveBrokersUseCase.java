package com.portifolio.apibff.usecases;

import com.portifolio.apibff.entities.domain.BrokerEntity;
import com.portifolio.apibff.entities.domain.StockEntity;

import java.util.List;


public interface RetrieveBrokersUseCase {

    List<BrokerEntity> getAll();

    BrokerEntity findById(Integer id);
}
