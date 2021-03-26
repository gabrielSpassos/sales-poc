package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.client.http.response.ScoreResponse;
import com.gabrielspassos.poc.dto.PersonScoreDTO;
import com.gabrielspassos.poc.entity.PersonScoreEntity;

public class ScoreStub {

    public static ScoreResponse createResponse(Integer score) {
        ScoreResponse scoreResponse = new ScoreResponse();
        scoreResponse.setScore(score);
        return scoreResponse;
    }

    public static String createResponseBody() {
        return "{\n" +
                "    \"score\": 33\n" +
                "}";
    }

    public static PersonScoreEntity createEntity(String id, String saleId, Integer score) {
        return PersonScoreEntity.builder()
                .id(id)
                .saleId(saleId)
                .score(score)
                .build();
    }

    public static PersonScoreDTO createDTO(String id, String saleId, Integer score) {
        return PersonScoreDTO.builder()
                .id(id)
                .saleId(saleId)
                .score(score)
                .build();
    }
}
