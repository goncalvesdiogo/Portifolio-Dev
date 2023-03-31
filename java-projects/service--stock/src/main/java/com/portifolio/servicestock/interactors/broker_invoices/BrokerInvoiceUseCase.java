package com.portifolio.servicestock.interactors.broker_invoices;

import com.portifolio.servicestock.entities.BrokerInvoiceEntity;

import java.util.List;

public interface BrokerInvoiceUseCase {
    List<BrokerInvoiceEntity> findAll();
    BrokerInvoiceEntity findById(Integer Id);
    BrokerInvoiceEntity create(BrokerInvoiceEntity entity);
    BrokerInvoiceEntity update(BrokerInvoiceEntity entity);
    void remove(Integer id);

}
