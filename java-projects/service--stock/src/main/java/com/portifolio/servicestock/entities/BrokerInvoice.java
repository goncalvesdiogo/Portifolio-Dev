package com.portifolio.servicestock.entities;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerInvoice {

    private Integer id;
    private Integer invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal totalSold;
    private BigDecimal totalBought;
    private BigDecimal totalLiquidationTax;
    private BigDecimal totalFees;
    private BigDecimal totalTax;
    private BigDecimal totalBrokerage;
    private Broker broker;
}
