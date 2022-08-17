package com.portifolio.servicestock.repositories.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FinantialMoventType {

    DEPOSITO_CORRETORA(1, "DEPOSITO CORRETORA", "C"),
    RENDIMENTOS(2, "RENDIMENTOS", "C"),
    JUROS_SOBRE_CAPITAL_PROPRIO(3, "JUROS SOBRE CAPITAL PROPRIO", "C"),
    DIVIDENDOS(4, "DIVIDENDOS", "C"),
    SAQUE_CORRETORA(5, "SAQUE CORRETORA", "D"),
    OPERACAO_COMPRA(6, "OPERAÇÃO - COMPRA", "D"),
    OPERACAO_VENDA(7, "OPERAÇÃO - VENDA", "C"),
    AMORTIZACAO(8, "AMORTIZAÇÃO", "C"),
    SUBSCRICAO_COMPRA(9, "SUBSCRIÇÃO - COMPRA", "D"),
    SUBSCRICAO_VENDA(10, "SUBSCRIÇÃO - VENDA", "C");

    private Integer id;
    private String description;
    private String movementType;
}
