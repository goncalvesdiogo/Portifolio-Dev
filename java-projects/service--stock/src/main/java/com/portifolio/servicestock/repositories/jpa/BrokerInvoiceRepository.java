package com.portifolio.servicestock.repositories.jpa;


import com.portifolio.servicestock.repositories.entities.BrokerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerInvoiceRepository extends JpaRepository<BrokerInvoice, Integer> {
}
