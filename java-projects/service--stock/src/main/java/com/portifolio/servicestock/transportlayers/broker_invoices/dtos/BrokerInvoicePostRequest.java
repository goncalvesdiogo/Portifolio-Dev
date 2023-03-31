package com.portifolio.servicestock.transportlayers.broker_invoices.dtos;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BrokerInvoicePostRequest {

    @NotNull(message = "Field invoiceNumber must be informed!")
    private Integer invoiceNumber;

    @NotNull(message = "Field invoiceDate must be informed!")
    private LocalDate invoiceDate;

    @NotNull(message = "Field totalSold must be informed!")
    private BigDecimal totalSold;

    @NotNull(message = "Field totalBought must be informed!")
    private BigDecimal totalBought;

    @NotNull(message = "Field totalLiquidationTax must be informed!")
    private BigDecimal totalLiquidationTax;

    @NotNull(message = "Field totalFees must be informed!")
    private BigDecimal totalFees;

    @NotNull(message = "Field totalTax must be informed!")
    private BigDecimal totalTax;

    @NotNull(message = "Field totalBrokerage must be informed!")
    private BigDecimal totalBrokerage;

    @NotNull(message = "Field broker must be informed!")
    @Valid
    private BrokerRequest broker;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Validated
    public static class BrokerRequest{

        @Min(value = 1)
        private Integer id;
    }

}
