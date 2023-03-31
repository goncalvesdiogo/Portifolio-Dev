package com.portifolio.servicestock.interactors.broker_invoices.impl;

import com.portifolio.servicestock.entities.BrokerInvoiceEntity;
import com.portifolio.servicestock.exceptions.ResourceNotFoundException;
import com.portifolio.servicestock.interactors.broker_invoices.BrokerInvoiceMapper;
import com.portifolio.servicestock.interactors.broker_invoices.BrokerInvoiceRepositoryMapper;
import com.portifolio.servicestock.interactors.broker_invoices.BrokerInvoiceUseCase;
import com.portifolio.servicestock.interactors.brokers.BrokerUseCase;
import com.portifolio.servicestock.repositories.jpa.BrokerInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@RequiredArgsConstructor
@Component
public class BrokerInvoiceUseCaseImpl implements BrokerInvoiceUseCase {

    private final BrokerInvoiceRepository repository;
    private final BrokerInvoiceRepositoryMapper mapper;
    private final BrokerInvoiceMapper invoiceMapper;
    private final BrokerUseCase brokerUseCase;
    @Override
    public List<BrokerInvoiceEntity> findAll() {
        return mapper.fromList(repository.findAll());
    }
    @Override
    public BrokerInvoiceEntity findById(Integer id) {
        var result = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Broker Invoice not found by Id:" + id));
        return invoiceMapper.from(result);
    }
    @Override
    public BrokerInvoiceEntity create(BrokerInvoiceEntity entity) {
        entity.setId(null);
        var broker = brokerUseCase.findById(entity.getBroker().getId());
        entity.setBroker(broker);
        var result = repository.save(invoiceMapper.from(entity));
        return invoiceMapper.from(result);
    }
    @Override
    public BrokerInvoiceEntity update(BrokerInvoiceEntity newEntity) {
        var oldEntity = findById(newEntity.getId());
        copyProperties(newEntity, oldEntity);
        var result = repository.save(invoiceMapper.from(oldEntity));
        return invoiceMapper.from(result);
    }
    @Override
    public void remove(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

}
