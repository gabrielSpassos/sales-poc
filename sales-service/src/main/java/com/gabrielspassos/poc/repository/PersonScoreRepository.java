package com.gabrielspassos.poc.repository;

import com.gabrielspassos.poc.entity.PersonScoreEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonScoreRepository extends ReactiveMongoRepository<PersonScoreEntity, String> {

    Mono<PersonScoreEntity> findBySaleId(String saleId);

}
