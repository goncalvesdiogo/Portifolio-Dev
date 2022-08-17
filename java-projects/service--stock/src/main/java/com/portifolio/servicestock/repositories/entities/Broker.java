package com.portifolio.servicestock.repositories.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "broker")
@Getter
@Setter
@NoArgsConstructor
public class Broker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "broker_short_name", nullable = false)
    private String brokerShortName;

    @Column(name = "broker_name", nullable = false)
    private String brokerName;

    @Column(name = "broker_cnpj", nullable = false)
    private String brokerCnpj;
}
