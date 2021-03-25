package com.gabrielspassos.poc.controller.v1;

import com.gabrielspassos.poc.controller.v1.request.PersonRequest;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.dto.SaleDTO;
import com.gabrielspassos.poc.service.SaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class SaleController implements BaseVersion {

    private final ModelMapper modelMapper;
    private final SaleService saleService;

    @PostMapping("/sales")
    public Mono<SaleDTO> createSale(@RequestBody PersonRequest personRequest) {
        log.info("Iniciado a criação de venda, {}", personRequest);
        PersonDTO personDTO = modelMapper.map(personRequest, PersonDTO.class);
        return saleService.createNewSale(personDTO)
                .doOnSuccess(response -> log.info("Criado venda {}", response));
    }
}
