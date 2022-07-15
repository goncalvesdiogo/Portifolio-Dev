package com.portifolio.apibff.usecases;

import com.portifolio.apibff.entities.domain.StockEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RetrieveStocksUseCase {

    List<StockEntity> execute();

}
