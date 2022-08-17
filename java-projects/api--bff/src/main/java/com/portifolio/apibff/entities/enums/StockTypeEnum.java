package com.portifolio.apibff.entities.enums;

import lombok.Getter;


@Getter

public enum StockTypeEnum {
    ACOES(1, "Ações"),
    FUNDOS_IMOBILIARIOS(2, "Fundos Imobiliários");

    StockTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    private final Integer code;
    private final String description;
}
