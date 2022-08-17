package com.portifolio.servicestock.repositories.jpa;


import com.portifolio.servicestock.repositories.entities.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Integer> {
}
