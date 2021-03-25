package com.gabrielspassos.poc.controller.v1;

import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.service.SaleService;
import com.gabrielspassos.poc.stub.PersonStub;
import com.gabrielspassos.poc.stub.SaleStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.gabrielspassos.poc.enumerator.SaleStatusEnum.LEAD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    private WebTestClient webTestClient;
    @Mock
    private SaleService saleService;
    @Spy
    private ModelMapper modelMapper;

    private static final String ID = "605ccdbc9dfc05477ff7970a";
    private static final String CPF = "06516227004";
    private static final String FIRST_NAME = "Jose";
    private static final String LAST_NAME = "Silva";
    private static final String EMAIL = "jose.silva@gmail.com";
    private static final LocalDate BIRTH_DATE = LocalDate.parse("1995-03-25");
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse("2021-03-25T14:51:56.274");

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient
                .bindToController(new SaleController(saleService, modelMapper))
                .configureClient()
                .build();
    }

    @Test
    public void shouldCreateSale() throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("src/test/resources/json/request/personRequest.json")));
        String output = new String(Files.readAllBytes(Paths.get("src/test/resources/json/response/saleResponse.json")));

        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(ID, LEAD, DATE_TIME, personDTO);

        given(saleService.createNewSale(personDTO))
                .willReturn(Mono.just(saleDTO));

        webTestClient.post()
                .uri("/v1/sales")
                .header("content-type", "application/json")
                .bodyValue(input)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(output);
    }

    @Test
    public void shouldGetSale() throws IOException {
        String output = new String(Files.readAllBytes(Paths.get("src/test/resources/json/response/saleResponse.json")));

        PersonDTO personDTO = PersonStub.createDTO(CPF, FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);
        SaleDTO saleDTO = SaleStub.createDTO(ID, LEAD, DATE_TIME, personDTO);

        given(saleService.getSaleById(ID))
                .willReturn(Mono.just(saleDTO));

        webTestClient.get()
                .uri("/v1/sales/{id}", ID)
                .header("content-type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(output);
    }

}