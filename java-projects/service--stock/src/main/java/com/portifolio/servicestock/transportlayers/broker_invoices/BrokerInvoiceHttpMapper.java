package com.portifolio.servicestock.transportlayers.broker_invoices;


import com.portifolio.servicestock.entities.BrokerInvoiceEntity;
import com.portifolio.servicestock.transportlayers.broker_invoices.dtos.BrokerInvoicePostRequest;
import com.portifolio.servicestock.transportlayers.broker_invoices.dtos.BrokerInvoiceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerInvoiceHttpMapper {

    List<BrokerInvoiceResponse> fromList(List<BrokerInvoiceEntity> entityList);

    BrokerInvoiceResponse from(BrokerInvoiceEntity entity);

    BrokerInvoiceEntity from(BrokerInvoicePostRequest request);


}
