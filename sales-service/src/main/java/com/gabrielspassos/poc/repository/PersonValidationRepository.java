package com.gabrielspassos.poc.repository;

import com.gabrielspassos.poc.entity.PersonValidationEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonValidationRepository extends ReactiveMongoRepository<PersonValidationEntity, String> {

    Mono<PersonValidationEntity> findBySaleId(String saleId);

}
