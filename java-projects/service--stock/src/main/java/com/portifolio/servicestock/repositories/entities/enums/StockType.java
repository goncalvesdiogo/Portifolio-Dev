package com.portifolio.servicestock.repositories.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockType {

    ACOES(1, "AÇÕES"),
    FUNDOS_IMOBILIARIOS(2, "AÇÕES");

    private Integer id;
    private String description;

}
