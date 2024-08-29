package com.portifolio.servicestock.repositories.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "broker_invoice")
@Getter
@Setter
@NoArgsConstructor
public class BrokerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "invoice_number", nullable = false)
    private Integer invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "total_sold", nullable = false)
    private BigDecimal totalSold;

    @Column(name = "total_bought", nullable = false)
    private BigDecimal totalBought;

    @Column(name = "total_liquidation_tax", nullable = false)
    private BigDecimal totalLiquidationTax;

    @Column(name = "total_fees", nullable = false)
    private BigDecimal totalFees;

    @Column(name = "total_tax", nullable = false)
    private BigDecimal totalTax;

    @Column(name = "total_brokerage", nullable = false)
    private BigDecimal totalBrokerage;

    @ManyToOne
    @JoinColumn(name = "broker_id", referencedColumnName = "id", nullable = false)
    private Broker broker;

}
