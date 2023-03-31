package com.portifolio.apibff.repositories.http;

import com.portifolio.apibff.repositories.dtos.BrokerPostRequest;
import com.portifolio.apibff.repositories.dtos.BrokerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "servicestock")
public interface ServiceStocksBrokersFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/service-stock/brokers")
    ResponseEntity<List<BrokerResponse>> getAll();

    @RequestMapping(method = RequestMethod.GET, value = "/service-stock/brokers/{id}")
    ResponseEntity<BrokerResponse> getById(@PathVariable("id") Integer id);


    /*@PostMapping
    ResponseEntity<BrokerResponse> create(@Valid @RequestBody BrokerPostRequest request);


    @PutMapping
    ResponseEntity<BrokerResponse> update(@PathVariable("id") Integer id, @Valid @RequestBody BrokerPostRequest request);

    @DeleteMapping
    ResponseEntity<Void> delete(@PathVariable("id") Integer id);


     */
}
