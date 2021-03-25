package com.gabrielspassos.poc.repository;

import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SaleRepository extends ReactiveMongoRepository<SaleEntity, String> {

    Flux<SaleEntity> findByStatus(SaleStatusEnum status);

}
