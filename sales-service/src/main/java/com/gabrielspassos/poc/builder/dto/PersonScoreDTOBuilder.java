package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.PersonScoreDTO;
import com.gabrielspassos.poc.entity.PersonScoreEntity;

public class PersonScoreDTOBuilder {

    public static PersonScoreDTO build(PersonScoreEntity entity) {
        return PersonScoreDTO.builder()
                .id(entity.getId())
                .saleId(entity.getSaleId())
                .score(entity.getScore())
                .build();
    }
}
