package com.portifolio.servicestock.interactors.broker_invoices;

import com.portifolio.servicestock.entities.BrokerInvoiceEntity;
import com.portifolio.servicestock.repositories.entities.BrokerInvoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrokerInvoiceMapper {
    BrokerInvoiceEntity from(BrokerInvoice obj);
    BrokerInvoice from (BrokerInvoiceEntity entity);
}
