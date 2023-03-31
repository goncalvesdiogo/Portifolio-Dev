package com.portifolio.servicestock.interactors.brokers;

import com.portifolio.servicestock.entities.BrokerEntity;

import java.util.List;

public interface BrokerUseCase {

    List<BrokerEntity> findAll();

    BrokerEntity findById(Integer Id);

     BrokerEntity create(BrokerEntity entity);

    BrokerEntity update(BrokerEntity entity);

    void remove(Integer id);
}
