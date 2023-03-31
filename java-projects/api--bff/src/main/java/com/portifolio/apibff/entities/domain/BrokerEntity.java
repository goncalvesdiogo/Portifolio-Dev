package com.portifolio.apibff.entities.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerEntity {

    private Integer id;
    private String brokerShortName;
    private String brokerName;
    private String brokerCnpj;

}
