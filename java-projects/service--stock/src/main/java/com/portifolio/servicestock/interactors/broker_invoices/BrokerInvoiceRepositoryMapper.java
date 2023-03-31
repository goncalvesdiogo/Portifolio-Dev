package com.portifolio.servicestock.interactors.broker_invoices;

import com.portifolio.servicestock.entities.BrokerInvoiceEntity;
import com.portifolio.servicestock.repositories.entities.BrokerInvoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = BrokerInvoiceMapper.class)
public interface BrokerInvoiceRepositoryMapper {
    List<BrokerInvoiceEntity> fromList (List<BrokerInvoice> list);

}
