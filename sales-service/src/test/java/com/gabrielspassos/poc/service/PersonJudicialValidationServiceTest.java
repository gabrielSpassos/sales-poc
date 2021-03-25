package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.client.http.JudicialServiceClient;
import com.gabrielspassos.poc.client.http.response.JudicialResponse;
import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.entity.PersonJudicialValidationEntity;
import com.gabrielspassos.poc.exception.NotFoundPersonJudicialValidationException;
import com.gabrielspassos.poc.repository.PersonJudicialValidationRepository;
import com.gabrielspassos.poc.stub.PersonJudicialStub;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.SaleStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import static com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PersonJudicialValidationServiceTest {

    @InjectMocks
    private PersonJudicialValidationService personJudicialValidationService;

    @Mock
    private JudicialServiceClient judicialServiceClient;

    @Mock
    private PersonJudicialValidationRepository personJudicialValidationRepository;

    private static final String ID = "dsydsf6523432ff";
    private static final String SALE_ID = "dfsg73462bdsaf";
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final String BIRTH_DATE = "1998-05-10";

    @Test
    public void shouldReturnJudicialValidationBySaleId() {
        PersonJudicialValidationEntity personJudicialValidationEntity = PersonJudicialStub.create(ID, SALE_ID, APPROVED);

        given(personJudicialValidationRepository.findBySaleId(SALE_ID)).willReturn(Mono.just(personJudicialValidationEntity));

        PersonJudicialValidationDTO personJudicialValidationDTO
                = personJudicialValidationService.getJudicialValidationBySaleId(SALE_ID).block();

        assertEquals(ID, personJudicialValidationDTO.getId());
        assertEquals(SALE_ID, personJudicialValidationDTO.getSaleId());
        assertEquals(APPROVED, personJudicialValidationDTO.getStatus());
    }

    @Test
    public void shouldThrowErrorForNotFoundEntityBySaleId() {
        given(personJudicialValidationRepository.findBySaleId(SALE_ID)).willReturn(Mono.empty());

        NotFoundPersonJudicialValidationException error = assertThrows(NotFoundPersonJudicialValidationException.class,
                () -> personJudicialValidationService.getJudicialValidationBySaleId(SALE_ID).block());

        assertEquals(HttpStatus.NOT_FOUND, error.getHttpStatus());
        assertEquals("Não encontrado validação judicial da pessoa", error.getMessage());
        assertEquals("2", error.getCode());
    }

    @Test
    public void shouldCreatePersonJudicialValidation() {
        PersonEvent personEvent = PersonStub.createEvent(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEvent saleEvent = SaleStub.createEvent(SALE_ID, personEvent);
        JudicialResponse judicialResponse = PersonJudicialStub.createResponse("STF", APPROVED);
        PersonJudicialValidationEntity personJudicialValidationEntity = PersonJudicialStub.create(null, SALE_ID, APPROVED);
        PersonJudicialValidationEntity savedEntity = PersonJudicialStub.create(ID, SALE_ID, APPROVED);

        given(judicialServiceClient.getJudicialValidationStatus(CPF)).willReturn(Mono.just(judicialResponse));
        given(personJudicialValidationRepository.save(personJudicialValidationEntity)).willReturn(Mono.just(savedEntity));

        PersonJudicialValidationDTO personJudicialValidationDTO = personJudicialValidationService.createPersonJudicialValidation(saleEvent).block();

        assertEquals(ID, personJudicialValidationDTO.getId());
        assertEquals(SALE_ID, personJudicialValidationDTO.getSaleId());
        assertEquals(APPROVED, personJudicialValidationDTO.getStatus());
    }
}