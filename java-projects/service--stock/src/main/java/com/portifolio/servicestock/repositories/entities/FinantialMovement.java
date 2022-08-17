package com.portifolio.servicestock.repositories.entities;

import com.portifolio.servicestock.repositories.entities.enums.FinantialMoventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "finantial_movement")
@Getter
@Setter
@NoArgsConstructor
public class FinantialMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "movement_date")
    private LocalDate movementDate;

    @Column(name = "finantial_movement_type_id")
    private FinantialMoventType finantialMovementType;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "broker_invoice_id", referencedColumnName = "id")
    private BrokerInvoice brokerInvoice;

    @ManyToOne
    @JoinColumn(name = "broker_id", referencedColumnName = "id")
    private Broker broker;


}
