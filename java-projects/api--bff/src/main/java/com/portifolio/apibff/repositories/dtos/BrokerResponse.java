package com.portifolio.apibff.repositories.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrokerResponse {

    private Integer id;
    private String brokerShortName;
    private String brokerName;
    private String brokerCnpj;
}
