package com.portifolio.servicestock.transportlayers.broker_invoices.dtos;

import com.portifolio.servicestock.entities.BrokerEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerInvoiceResponse {

    private Integer id;
    private Integer invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal totalSold;
    private BigDecimal totalBought;
    private BigDecimal totalLiquidationTax;
    private BigDecimal totalFees;
    private BigDecimal totalTax;
    private BigDecimal totalBrokerage;
    private BrokerEntity broker;

}
