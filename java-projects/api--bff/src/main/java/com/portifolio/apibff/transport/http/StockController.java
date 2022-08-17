package com.portifolio.apibff.transport.http;

import com.portifolio.apibff.api.StocksApi;
import com.portifolio.apibff.exceptions.ResourceNotFoundException;
import com.portifolio.apibff.model.Stock;
import com.portifolio.apibff.transport.mappers.StockMapper;
import com.portifolio.apibff.usecases.RetrieveStocksUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
    public ResponseEntity<Stock> getStockById(Integer id) {
        var stockEntity = useCase.findById(id);
        if(ObjectUtils.isEmpty(stockEntity)){
            throw new ResourceNotFoundException("Not found Stock Id: " + id);
        }
        var stock = mapper.from(stockEntity);
        return ResponseEntity.ok().body(stock);
    }

    @Override
    public ResponseEntity<List<Stock>> getStocks() {
        var stockEntityList = useCase.execute();
        if(ObjectUtils.isEmpty(stockEntityList)){
            return ResponseEntity.noContent().build();
        }
        var stockList = stockEntityList.stream().map(mapper::from).collect(Collectors.toList());
        return ResponseEntity.ok().body(stockList);
    }

}
