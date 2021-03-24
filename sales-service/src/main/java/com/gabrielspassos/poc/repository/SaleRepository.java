package com.gabrielspassos.poc.repository;

import com.gabrielspassos.poc.entity.SaleEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends ReactiveMongoRepository<SaleEntity, String> {
}
