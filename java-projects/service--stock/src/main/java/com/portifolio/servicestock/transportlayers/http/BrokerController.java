package com.portifolio.servicestock.transportlayers.http;

import com.portifolio.servicestock.interactors.BrokerUseCase;
import com.portifolio.servicestock.transportlayers.BrokerHttpMapper;
import com.portifolio.servicestock.transportlayers.dtos.BrokerDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brokers")
@RequiredArgsConstructor
public class BrokerController {

    private final BrokerUseCase useCase;
    private final BrokerHttpMapper mapper;

    @GetMapping
    public ResponseEntity<List<BrokerDAO>> getBrokersList(){
        var result = mapper.map(useCase.getAllList());
            return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
