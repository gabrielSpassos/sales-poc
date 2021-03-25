package com.gabrielspassos.poc.service;

import com.gabrielspassos.poc.builder.dto.PersonScoreDTOBuilder;
import com.gabrielspassos.poc.builder.entity.PersonScoreEntityBuilder;
import com.gabrielspassos.poc.client.http.AnalysisServiceClient;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.PersonScoreDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.repository.PersonScoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class PersonScoreService {

    private final AnalysisServiceClient analysisServiceClient;
    private final PersonScoreRepository personScoreRepository;

    public Mono<PersonScoreDTO> savePersonScore(SaleDTO saleDTO) {
        String saleId = saleDTO.getId();
        PersonDTO person = saleDTO.getPerson();

        return analysisServiceClient.getPersonScore(person.getNationalIdentificationNumber())
                .map(scoreResponse -> PersonScoreEntityBuilder.build(saleId, scoreResponse))
                .flatMap(personScoreRepository::save)
                .map(PersonScoreDTOBuilder::build)
                .doOnSuccess(dto -> log.info("Salvo score da venda {}: {}", saleId, dto));
    }
}
