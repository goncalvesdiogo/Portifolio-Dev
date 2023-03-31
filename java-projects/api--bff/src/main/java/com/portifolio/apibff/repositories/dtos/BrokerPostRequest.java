package com.portifolio.apibff.repositories.dtos;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BrokerPostRequest {

    @NotBlank(message = "Field brokerShortName must be informed!")
    private String brokerShortName;

    @NotBlank(message = "Field brokerName must be informed!")
    private String brokerName;

    @NotBlank(message = "Field brokerCnpj must be informed!")
    private String brokerCnpj;
}
