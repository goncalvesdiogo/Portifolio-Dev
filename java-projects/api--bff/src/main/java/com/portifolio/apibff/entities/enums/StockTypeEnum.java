package com.portifolio.apibff.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum StockTypeEnum {
    ACOES(1, "Ações"),
    FUNDOS_IMOBILIARIOS(2, "Fundos Imobiliários");

    private int code;
    private String description;
}
