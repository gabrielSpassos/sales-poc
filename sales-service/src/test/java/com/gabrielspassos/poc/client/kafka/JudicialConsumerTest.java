package com.gabrielspassos.poc.client.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielspassos.poc.client.kafka.event.PersonEvent;
import com.gabrielspassos.poc.client.kafka.event.SaleEvent;
import com.gabrielspassos.poc.dto.PersonJudicialValidationDTO;
import com.gabrielspassos.poc.enumerator.PersonJudicialValidationStatusEnum;
import com.gabrielspassos.poc.service.PersonJudicialValidationService;
import com.gabrielspassos.poc.stub.PersonJudicialStub;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.SaleStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JudicialConsumerTest {

    @InjectMocks
    private JudicialConsumer judicialConsumer;

    @Mock
    private PersonJudicialValidationService personJudicialValidationService;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private Acknowledgment ack;

    private static final String SALE_ID = "dfsg73462bdsaf";
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final String BIRTH_DATE = "1998-05-10";

    @Test
    public void shouldCreateJudicialValidation() {
        String event = "{\"id\":\"dfsg73462bdsaf\",\"person\":{\"nationalIdentificationNumber\":\"94206237000\",\"birthdate\":\"1998-05-10\",\"firstName\":\"Jose\",\"lastName\":\"Souza\",\"email\":\"jose.souza@gmail.com\"}}";
        PersonEvent personEvent = PersonStub.createEvent(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleEvent saleEvent = SaleStub.createEvent(SALE_ID, personEvent);
        PersonJudicialValidationDTO personJudicialValidationDTO
                = PersonJudicialStub.createDTO("judicialId", SALE_ID, PersonJudicialValidationStatusEnum.APPROVED);

        given(personJudicialValidationService.createPersonJudicialValidation(saleEvent))
                .willReturn(Mono.just(personJudicialValidationDTO));

        judicialConsumer.listen(event, ack, "0", "0");

        verify(personJudicialValidationService).createPersonJudicialValidation(saleEvent);
        verify(ack).acknowledge();
    }

    @Test
    public void shouldThrowErrorToParseEvent() {
        assertThrows(IllegalStateException.class, () -> judicialConsumer.listen("", ack, "0", "0"));

        verify(personJudicialValidationService, never()).createPersonJudicialValidation(any());
        verify(ack, never()).acknowledge();
    }

}