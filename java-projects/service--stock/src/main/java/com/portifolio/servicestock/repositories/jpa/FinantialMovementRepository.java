package com.portifolio.servicestock.repositories.jpa;


import com.portifolio.servicestock.repositories.entities.FinantialMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinantialMovementRepository extends JpaRepository<FinantialMovement, Integer> {
}
