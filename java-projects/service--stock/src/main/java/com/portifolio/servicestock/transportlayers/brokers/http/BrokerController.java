package com.portifolio.servicestock.transportlayers.brokers.http;

import com.portifolio.servicestock.interactors.brokers.BrokerUseCase;
import com.portifolio.servicestock.transportlayers.brokers.BrokerHttpMapper;
import com.portifolio.servicestock.transportlayers.brokers.dtos.BrokerPostRequest;
import com.portifolio.servicestock.transportlayers.brokers.dtos.BrokerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/brokers")
@RequiredArgsConstructor
public class BrokerController {

    private final BrokerUseCase useCase;
    private final BrokerHttpMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BrokerResponse> create(@Valid @RequestBody BrokerPostRequest request){
        var brokerEntity = mapper.from(request);
        var result = mapper.from(useCase.create(brokerEntity));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BrokerResponse>> getAll(){
        var result = mapper.fromList(useCase.findAll());
            return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BrokerResponse> getById(@PathVariable("id") Integer id){
        var result = mapper.from(useCase.findById(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BrokerResponse> update(@PathVariable("id") Integer id, @Valid @RequestBody BrokerPostRequest request){
        var brokerEntity = mapper.from(request);
        brokerEntity.setId(id);
        var result = mapper.from(useCase.update(brokerEntity));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        useCase.remove(id);
        return ResponseEntity.noContent().build();
    }

}
