package com.portifolio.apibff.transport.mappers;

import com.portifolio.apibff.entities.domain.Stock;
import com.portifolio.apibff.entities.domain.StockEntity;
import org.mapstruct.Mapper;

@Mapper
public interface StockMapper {

    Stock from(StockEntity stockEntity);
}
