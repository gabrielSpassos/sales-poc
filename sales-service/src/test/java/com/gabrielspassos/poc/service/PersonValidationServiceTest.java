package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.client.http.PersonServiceClient;
import com.gabrielspassos.poc.client.http.response.PersonResponse;
import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonValidationDTO;
import com.gabrielspassos.poc.entity.PersonValidationEntity;
import com.gabrielspassos.poc.exception.NotFoundPersonValidationException;
import com.gabrielspassos.poc.repository.PersonValidationRepository;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.PersonValidationStub;
import com.gabrielspassos.poc.stub.SaleStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PersonValidationServiceTest {

    @InjectMocks
    private PersonValidationService personValidationService;

    @Mock
    private PersonServiceClient personServiceClient;

    @Mock
    private PersonValidationRepository personValidationRepository;

    private static final String ID = "dshgdas6253gsda";
    private static final String SALE_ID = "dfsg73462bdsaf";
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final String BIRTH_DATE = "1998-05-10";

    @Test
    public void shouldGetPersonValidationBySaleId() {
        PersonValidationEntity entity = PersonValidationStub.createEntity(ID, SALE_ID, APPROVED);

        given(personValidationRepository.findBySaleId(SALE_ID)).willReturn(Mono.just(entity));

        PersonValidationDTO personValidationDTO = personValidationService.getPersonValidationBySaleId(SALE_ID).block();

        assertEquals(ID, personValidationDTO.getId());
        assertEquals(SALE_ID, personValidationDTO.getSaleId());
        assertEquals(APPROVED, personValidationDTO.getStatus());
    }

    @Test
    public void shouldThrowErrorForNotFoundEntity() {
        given(personValidationRepository.findBySaleId(SALE_ID)).willReturn(Mono.empty());

        NotFoundPersonValidationException error = assertThrows(NotFoundPersonValidationException.class,
                () -> personValidationService.getPersonValidationBySaleId(SALE_ID).block());

        assertEquals(HttpStatus.NOT_FOUND, error.getHttpStatus());
        assertEquals("Não encontrado validação da pessoa", error.getMessage());
        assertEquals("1", error.getCode());
    }

    @Test
    public void shouldCreatePersonValidation() {
        PersonEvent personEvent = PersonStub.createEvent(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEvent saleEvent = SaleStub.createEvent(SALE_ID, personEvent);
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, LocalDate.parse(BIRTH_DATE));
        PersonResponse personResponse = PersonValidationStub.createResponse(APPROVED);
        PersonValidationEntity personValidationEntity = PersonValidationStub.createEntity(null, SALE_ID, APPROVED);
        PersonValidationEntity savedEntity = PersonValidationStub.createEntity(ID, SALE_ID, APPROVED);

        given(personServiceClient.getPersonValidationStatus(personDTO)).willReturn(Mono.just(personResponse));
        given(personValidationRepository.save(personValidationEntity)).willReturn(Mono.just(savedEntity));

        PersonValidationDTO personValidationDTO = personValidationService.createPersonValidation(saleEvent).block();

        assertEquals(ID, personValidationDTO.getId());
        assertEquals(SALE_ID, personValidationDTO.getSaleId());
        assertEquals(APPROVED, personValidationDTO.getStatus());
    }

}