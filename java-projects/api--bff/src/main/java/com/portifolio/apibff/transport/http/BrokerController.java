package com.portifolio.apibff.transport.http;

import com.portifolio.apibff.api.BrokersApi;
import com.portifolio.apibff.exceptions.ResourceNotFoundException;
import com.portifolio.apibff.model.Broker;
import com.portifolio.apibff.transport.mappers.BrokerEntityToDtoMapper;
import com.portifolio.apibff.usecases.RetrieveBrokersUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BrokerController implements BrokersApi {

    private final RetrieveBrokersUseCase useCase;
    private final BrokerEntityToDtoMapper mapper;

    @Override
    public ResponseEntity<Broker> getBrokerById(Integer id) {
        var entity = useCase.findById(id);
        if(ObjectUtils.isEmpty(entity)){
            throw new ResourceNotFoundException("Not found Stock Id: " + id);
        }
        return ResponseEntity.ok().body(mapper.from(entity));
    }

    @Override
    public ResponseEntity<List<Broker>> getBrokers() {
        var entityList = useCase.getAll();
        if(ObjectUtils.isEmpty(entityList)){
            return ResponseEntity.noContent().build();
        }
        var list = entityList.stream().map(mapper::from).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

}
