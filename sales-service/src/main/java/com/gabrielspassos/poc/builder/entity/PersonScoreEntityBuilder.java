package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.client.http.response.ScoreResponse;
import com.gabrielspassos.poc.entity.PersonScoreEntity;

public class PersonScoreEntityBuilder {

    public static PersonScoreEntity build(String saleId, ScoreResponse scoreResponse) {
        return PersonScoreEntity.builder()
                .id(null)
                .saleId(saleId)
                .score(scoreResponse.getScore())
                .build();
    }
}
