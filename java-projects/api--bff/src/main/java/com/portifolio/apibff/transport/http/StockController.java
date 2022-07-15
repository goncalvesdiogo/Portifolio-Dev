package com.portifolio.apibff.transport.http;

import com.portifolio.apibff.entities.domain.Stock;
import com.portifolio.apibff.transport.mappers.StockMapper;
import com.portifolio.apibff.transport.openapi.StocksApi;
import com.portifolio.apibff.usecases.RetrieveStocksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StockController implements StocksApi {

    private final RetrieveStocksUseCase useCase;
    private final StockMapper mapper;

    @Override
    public ResponseEntity<List<Stock>> getStocks() {
        var stockEntityList = useCase.execute();
        if(stockEntityList.isEmpty()){
            //throw new DataNotFoundException("");
        }
        var stockList = stockEntityList.stream().map(mapper::from).collect(Collectors.toList());
        return ResponseEntity.ok().body(stockList);
    }
}
