package com.portifolio.apibff.usecases;

import com.portifolio.apibff.entities.domain.StockEntity;

import java.util.List;


public interface RetrieveStocksUseCase {

    List<StockEntity> execute();

    StockEntity findById(Integer id);
}
