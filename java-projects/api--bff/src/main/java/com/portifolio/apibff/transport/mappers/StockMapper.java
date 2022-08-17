package com.portifolio.apibff.transport.mappers;

import com.portifolio.apibff.entities.domain.StockEntity;
import com.portifolio.apibff.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mappings({@Mapping(target="stockType", source="stockType.code")})
    Stock from(StockEntity stockEntity);
}
