package com.portifolio.servicestock.transportlayers.broker_invoices.http;

import com.portifolio.servicestock.interactors.broker_invoices.BrokerInvoiceUseCase;
import com.portifolio.servicestock.transportlayers.broker_invoices.BrokerInvoiceHttpMapper;
import com.portifolio.servicestock.transportlayers.broker_invoices.dtos.BrokerInvoicePostRequest;
import com.portifolio.servicestock.transportlayers.broker_invoices.dtos.BrokerInvoiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/broker-invoices")
@RequiredArgsConstructor
public class BrokerInvoiceController {

    private final BrokerInvoiceUseCase useCase;
    private final BrokerInvoiceHttpMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BrokerInvoiceResponse> create(@Valid @RequestBody BrokerInvoicePostRequest request){
        var entity = mapper.from(request);
        var result = mapper.from(useCase.create(entity));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BrokerInvoiceResponse>> getAll(){
        var result = mapper.fromList(useCase.findAll());
            return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BrokerInvoiceResponse> getById(@PathVariable("id") Integer id){
        var result = mapper.from(useCase.findById(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BrokerInvoiceResponse> update(@PathVariable("id") Integer id, @Valid @RequestBody BrokerInvoicePostRequest request){
        var entity = mapper.from(request);
        entity.setId(id);
        var result = mapper.from(useCase.update(entity));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        useCase.remove(id);
        return ResponseEntity.noContent().build();
    }

}
