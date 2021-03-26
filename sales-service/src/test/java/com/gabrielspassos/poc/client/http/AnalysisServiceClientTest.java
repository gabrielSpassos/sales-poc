package com.gabrielspassos.poc.client.http;

import com.gabrielspassos.poc.client.http.response.ScoreResponse;
import com.gabrielspassos.poc.exception.NotFoundPersonException;
import com.gabrielspassos.poc.exception.UnexpectedInternalException;
import com.gabrielspassos.poc.stub.ScoreStub;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceClientTest {

    private AnalysisServiceClient analysisServiceClient;
    private final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(6000));
    private static final String CPF = "94206237000";

    @BeforeEach
    public void setup() {
        this.analysisServiceClient = new AnalysisServiceClient("http://localhost:6000");
        this.wireMockServer.start();
    }

    @AfterEach
    public void tearDown() {
        this.wireMockServer.stop();
    }

    @Test
    public void shouldReturnPersonScore() {
        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000/scores"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())
                        .withBody(ScoreStub.createResponseBody())));

        ScoreResponse response = analysisServiceClient.getPersonScore(CPF).block();

        assertNotNull(response);
        assertEquals(33, response.getScore());
    }

    @Test
    public void shouldThrowNotFoundError() {
        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000/scores"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withBody("")));

        assertThrows(NotFoundPersonException.class, () -> analysisServiceClient.getPersonScore(CPF).block());
    }

    @Test
    public void shouldThrowInternalError() {
        wireMockServer.stubFor(get(urlPathEqualTo("/people/94206237000/scores"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody("")));

        assertThrows(UnexpectedInternalException.class, () -> analysisServiceClient.getPersonScore(CPF).block());
    }

}