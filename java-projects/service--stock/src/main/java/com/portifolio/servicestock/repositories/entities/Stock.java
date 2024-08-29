package com.portifolio.servicestock.repositories.entities;

import com.portifolio.servicestock.repositories.entities.enums.StockType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "id_api")
    private Integer idApi;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "company_cnpj", nullable = false)
    private String companyCnpj;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "holder_company_cnpj")
    private String holderCompanyCnpj;

    @Column(name = "holder_company_name")
    private String holderCompanyName;

    @Column(name = "stock_type_id", nullable = false)
    private StockType stockTypeId;

}
