package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.client.http.AnalysisServiceClient;
import com.gabrielspassos.poc.client.http.response.ScoreResponse;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonScoreDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.entity.PersonScoreEntity;
import com.gabrielspassos.poc.enumerator.SaleStatusEnum;
import com.gabrielspassos.poc.repository.PersonScoreRepository;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.SaleStub;
import com.gabrielspassos.poc.stub.ScoreStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PersonScoreServiceTest {

    @InjectMocks
    private PersonScoreService personScoreService;

    @Mock
    private AnalysisServiceClient analysisServiceClient;

    @Mock
    private PersonScoreRepository personScoreRepository;

    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final LocalDate BIRTH_DATE = LocalDate.parse("1998-05-10");
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse("2021-03-25T10:15:30");
    private static final String SALE_ID = "dfsg73462bdsaf";
    private static final String SCORE_ID = "dhsgd56532gd";
    private static final Integer SCORE = 61;
    private static final ScoreResponse SCORE_RESPONSE = ScoreStub.createResponse(SCORE);

    @Test
    public void shouldPersonScore() {
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(SALE_ID, SaleStatusEnum.LEAD, DATE_TIME, personDTO);
        PersonScoreEntity personScoreEntity = ScoreStub.createEntity(null, SALE_ID, SCORE);
        PersonScoreEntity savedScoreEntity = ScoreStub.createEntity("dhsgd56532gd", SALE_ID, SCORE);

        given(analysisServiceClient.getPersonScore(CPF)).willReturn(Mono.just(SCORE_RESPONSE));
        given(personScoreRepository.save(personScoreEntity)).willReturn(Mono.just(savedScoreEntity));

        PersonScoreDTO block = personScoreService.savePersonScore(saleDTO).block();

        assertEquals(SCORE_ID, block.getId());
        assertEquals(SALE_ID, block.getSaleId());
        assertEquals(SCORE, block.getScore());
    }

}