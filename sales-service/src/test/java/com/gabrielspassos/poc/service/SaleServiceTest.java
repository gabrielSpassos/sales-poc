package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.client.kafka.JudicialProducer;
import com.gabrielspassos.poc.client.kafka.PersonProducer;
import com.gabrielspassos.poc.config.SaleConfig;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.dto.PersonScoreDTO;
import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.entity.PersonEntity;
import com.gabrielspassos.poc.entity.SaleEntity;
import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import com.gabrielspassos.poc.repository.SaleRepository;
import com.gabrielspassos.poc.stub.PersonJudicialStub;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.PersonValidationStub;
import com.gabrielspassos.poc.stub.SaleStub;
import com.gabrielspassos.poc.stub.ScoreStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.gabrielspassos.poc.enumerator.SaleStatusEnum.LEAD;
import static com.gabrielspassos.poc.enumerator.SaleStatusEnum.PROSPECT;
import static com.gabrielspassos.poc.enumerator.SaleStatusEnum.REJECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private PersonValidationService personValidationService;

    @Mock
    private PersonJudicialValidationService personJudicialValidationService;

    @Mock
    private PersonScoreService personScoreService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private PersonProducer personProducer;

    @Mock
    private JudicialProducer judicialProducer;

    @Mock
    private SaleConfig saleConfig;

    private static final String ID = "dfsg73462bdsaf";
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final LocalDate BIRTH_DATE = LocalDate.parse("1998-05-10");
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse("2021-03-25T10:15:30");
    private static final Integer MINIMUM_SCORE = 61;

    @Test
    public void shouldCreateNewSale() {
        ArgumentCaptor<SaleEntity> argumentCaptor = ArgumentCaptor.forClass(SaleEntity.class);
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(ID, LEAD, DATE_TIME, personDTO);
        PersonEntity personEntity = PersonStub.createEntity(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEntity entity = SaleStub.createEntity(ID, LEAD, DATE_TIME, personEntity);

        given(saleRepository.save(argumentCaptor.capture())).willReturn(Mono.just(entity));
        given(personProducer.sendVoteToTopic(saleDTO)).willReturn(Mono.just(saleDTO));
        given(judicialProducer.sendVoteToTopic(saleDTO)).willReturn(Mono.just(saleDTO));

        SaleDTO sale = saleService.createNewSale(personDTO).block();

        SaleEntity value = argumentCaptor.getValue();

        verify(personProducer).sendVoteToTopic(saleDTO);
        verify(judicialProducer).sendVoteToTopic(saleDTO);

        assertEquals(ID, sale.getId());
        assertEquals(LEAD, sale.getStatus());

        assertNull(value.getId());
        assertEquals(LEAD, value.getStatus());
        assertNotNull(value.getRegisterDateTime());
        assertEquals(personEntity, value.getPerson());
    }

    @Test
    public void shouldAnalyzeSalesScoresAndTurnSaleToProspectStatus() {
        PersonEntity personEntity = PersonStub.createEntity(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEntity leadEntity = SaleStub.createEntity(ID, LEAD, DATE_TIME, personEntity);
        SaleEntity prospectEntity = SaleStub.createEntity(ID, PROSPECT, DATE_TIME, personEntity);
        PersonJudicialValidationDTO personJudicialValidationDTO
                = PersonJudicialStub.createDTO("judicialId", ID, PersonJudicialValidationStatusEnum.APPROVED);
        PersonValidationDTO personValidationDTO
                = PersonValidationStub.createDTO("personId", ID, PersonValidationStatusEnum.APPROVED);
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(ID, LEAD, DATE_TIME, personDTO);
        PersonScoreDTO personScoreDTO = ScoreStub.createDTO("scoreId", ID, MINIMUM_SCORE);

        given(saleRepository.findByStatus(LEAD)).willReturn(Flux.just(leadEntity));
        given(personJudicialValidationService.getJudicialValidationBySaleId(ID))
                .willReturn(Mono.just(personJudicialValidationDTO));
        given(personValidationService.getPersonValidationBySaleId(ID))
                .willReturn(Mono.just(personValidationDTO));
        given(personScoreService.createPersonScore(saleDTO))
                .willReturn(Mono.just(personScoreDTO));
        given(saleConfig.getMinimumValidScore()).willReturn(MINIMUM_SCORE);
        given(saleRepository.save(prospectEntity)).willReturn(Mono.just(prospectEntity));

        List<SaleDTO> sales = saleService.analyzeSalesScores().collectList().block();

        assertEquals(1, sales.size());
        assertEquals(PROSPECT, sales.get(0).getStatus());
    }

    @Test
    public void shouldAnalyzeSalesScoresAndTurnSaleToRejectStatusForNotValidJudicialStatus() {
        PersonEntity personEntity = PersonStub.createEntity(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEntity leadEntity = SaleStub.createEntity(ID, LEAD, DATE_TIME, personEntity);
        SaleEntity rejectEntity = SaleStub.createEntity(ID, REJECT, DATE_TIME, personEntity);
        PersonJudicialValidationDTO personJudicialValidationDTO
                = PersonJudicialStub.createDTO("judicialId", ID, PersonJudicialValidationStatusEnum.REPROVED);
        PersonValidationDTO personValidationDTO
                = PersonValidationStub.createDTO("personId", ID, PersonValidationStatusEnum.APPROVED);

        given(saleRepository.findByStatus(LEAD)).willReturn(Flux.just(leadEntity));
        given(personJudicialValidationService.getJudicialValidationBySaleId(ID))
                .willReturn(Mono.just(personJudicialValidationDTO));
        given(personValidationService.getPersonValidationBySaleId(ID))
                .willReturn(Mono.just(personValidationDTO));
        given(saleRepository.save(rejectEntity)).willReturn(Mono.just(rejectEntity));

        List<SaleDTO> sales = saleService.analyzeSalesScores().collectList().block();

        verifyNoInteractions(personScoreService, saleConfig);
        assertEquals(1, sales.size());
        assertEquals(REJECT, sales.get(0).getStatus());
    }

    @Test
    public void shouldAnalyzeSalesScoresAndTurnSaleToRejectStatusForNotValidPersonStatus() {
        PersonEntity personEntity = PersonStub.createEntity(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEntity leadEntity = SaleStub.createEntity(ID, LEAD, DATE_TIME, personEntity);
        SaleEntity rejectEntity = SaleStub.createEntity(ID, REJECT, DATE_TIME, personEntity);
        PersonJudicialValidationDTO personJudicialValidationDTO
                = PersonJudicialStub.createDTO("judicialId", ID, PersonJudicialValidationStatusEnum.APPROVED);
        PersonValidationDTO personValidationDTO
                = PersonValidationStub.createDTO("personId", ID, PersonValidationStatusEnum.REPROVED);

        given(saleRepository.findByStatus(LEAD)).willReturn(Flux.just(leadEntity));
        given(personJudicialValidationService.getJudicialValidationBySaleId(ID))
                .willReturn(Mono.just(personJudicialValidationDTO));
        given(personValidationService.getPersonValidationBySaleId(ID))
                .willReturn(Mono.just(personValidationDTO));
        given(saleRepository.save(rejectEntity)).willReturn(Mono.just(rejectEntity));

        List<SaleDTO> sales = saleService.analyzeSalesScores().collectList().block();

        verifyNoInteractions(personScoreService, saleConfig);
        assertEquals(1, sales.size());
        assertEquals(REJECT, sales.get(0).getStatus());
    }

    @Test
    public void shouldAnalyzeSalesScoresAndTurnSaleToRejectStatusForNotValidScore() {
        PersonEntity personEntity = PersonStub.createEntity(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEntity leadEntity = SaleStub.createEntity(ID, LEAD, DATE_TIME, personEntity);
        SaleEntity rejectEntity = SaleStub.createEntity(ID, REJECT, DATE_TIME, personEntity);
        PersonJudicialValidationDTO personJudicialValidationDTO
                = PersonJudicialStub.createDTO("judicialId", ID, PersonJudicialValidationStatusEnum.APPROVED);
        PersonValidationDTO personValidationDTO
                = PersonValidationStub.createDTO("personId", ID, PersonValidationStatusEnum.APPROVED);
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(ID, LEAD, DATE_TIME, personDTO);
        PersonScoreDTO personScoreDTO = ScoreStub.createDTO("scoreId", ID, MINIMUM_SCORE - 1);

        given(saleRepository.findByStatus(LEAD)).willReturn(Flux.just(leadEntity));
        given(personJudicialValidationService.getJudicialValidationBySaleId(ID))
                .willReturn(Mono.just(personJudicialValidationDTO));
        given(personValidationService.getPersonValidationBySaleId(ID))
                .willReturn(Mono.just(personValidationDTO));
        given(personScoreService.createPersonScore(saleDTO))
                .willReturn(Mono.just(personScoreDTO));
        given(saleConfig.getMinimumValidScore()).willReturn(MINIMUM_SCORE);
        given(saleRepository.save(rejectEntity)).willReturn(Mono.just(rejectEntity));

        List<SaleDTO> sales = saleService.analyzeSalesScores().collectList().block();

        assertEquals(1, sales.size());
        assertEquals(REJECT, sales.get(0).getStatus());
    }

}