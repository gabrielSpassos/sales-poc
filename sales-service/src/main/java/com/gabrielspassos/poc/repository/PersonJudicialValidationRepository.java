package com.gabrielspassos.poc.repository;

import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonJudicialValidationRepository extends ReactiveMongoRepository<PersonJudicialValidationEntity, String> {

    Mono<PersonJudicialValidationEntity> findBySaleId(String saleId);

}
