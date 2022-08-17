package com.portifolio.servicestock.transportlayers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerDAO {

    private Integer id;
    private String brokerShortName;
    private String brokerName;
    private String brokerCnpj;
}
