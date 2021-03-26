package com.gabrielspassos.poc.client.http;

import com.gabrielspassos.poc.client.http.response.PersonResponse;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.enumerator.PersonValidationStatusEnum;
import com.gabrielspassos.poc.exception.NotFoundPersonException;
import com.gabrielspassos.poc.exception.UnexpectedInternalException;
import com.gabrielspassos.poc.stub.PersonJudicialStub;
import com.gabrielspassos.poc.stub.PersonStub;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PersonServiceClientTest {

    private PersonServiceClient personServiceClient;
    private final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(6002));
    private static final String CPF = "94206237000";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Souza";
    private static final String EMAIL = "jose.souza@gmail.com";
    private static final LocalDate BIRTH_DATE = LocalDate.parse("1998-05-10");

    @BeforeEach
    public void setup() {
        this.personServiceClient = new PersonServiceClient("http://localhost:6002");
        this.wireMockServer.start();
    }

    @AfterEach
    public void tearDown() {
        this.wireMockServer.stop();
    }

    @Test
    public void shouldReturnPersonScore() {
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);

        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000"))
                .withQueryParam("firstName", equalTo(FIRST_NAME))
                .withQueryParam("lastName", equalTo(LAST_NAME))
                .withQueryParam("birthdate", equalTo(BIRTH_DATE.toString()))
                .withQueryParam("email", equalTo(EMAIL))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(PersonJudicialStub.createResponseBody())));

        PersonResponse response = personServiceClient.getPersonValidationStatus(personDTO).block();

        assertNotNull(response);
        assertEquals(PersonValidationStatusEnum.REPROVED, response.getStatus());
    }

    @Test
    public void shouldThrowNotFoundError() {
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);

        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000"))
                .withQueryParam("firstName", equalTo(FIRST_NAME))
                .withQueryParam("lastName", equalTo(LAST_NAME))
                .withQueryParam("birthdate", equalTo(BIRTH_DATE.toString()))
                .withQueryParam("email", equalTo(EMAIL))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody("")));

        assertThrows(NotFoundPersonException.class, () -> personServiceClient.getPersonValidationStatus(personDTO).block());
    }

    @Test
    public void shouldThrowInternalError() {
        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);

        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000"))
                .withQueryParam("firstName", equalTo(FIRST_NAME))
                .withQueryParam("lastName", equalTo(LAST_NAME))
                .withQueryParam("birthdate", equalTo(BIRTH_DATE.toString()))
                .withQueryParam("email", equalTo(EMAIL))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody("")));

        assertThrows(UnexpectedInternalException.class, () -> personServiceClient.getPersonValidationStatus(personDTO).block());
    }

}