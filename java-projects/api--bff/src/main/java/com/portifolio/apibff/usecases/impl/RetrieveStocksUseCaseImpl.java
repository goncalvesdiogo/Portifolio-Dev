package com.portifolio.apibff.usecases.impl;

import com.portifolio.apibff.entities.domain.StockEntity;
import com.portifolio.apibff.usecases.RetrieveStocksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RetrieveStocksUseCaseImpl implements RetrieveStocksUseCase {

    @Override
    public List<StockEntity> execute() {
        return null;
    }

    @Override
    public StockEntity findById(Integer id) {
        return null;
    }


}
