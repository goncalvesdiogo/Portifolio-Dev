package com.portifolio.servicestock.entities;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinantialMoventTypeEntity {

    private Integer id;

    private String description;

    private String movementType;
}
