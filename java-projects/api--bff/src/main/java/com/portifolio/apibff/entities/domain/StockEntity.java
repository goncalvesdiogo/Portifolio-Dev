package com.portifolio.apibff.entities.domain;

import com.portifolio.apibff.entities.enums.StockTypeEnum;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockEntity {

    private Integer id;
    private Integer idApi;
    private String code;
    private String companyName;
    private String companyCnpj;
    private String holderCompanyName;
    private String holderCompanyCnpj;
    private StockTypeEnum stockType;

}
