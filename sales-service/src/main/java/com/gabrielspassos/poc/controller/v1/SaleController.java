package com.gabrielspassos.poc.controller.v1;

import com.gabrielspassos.poc.controller.v1.request.PersonRequest;
import com.gabrielspassos.poc.controller.v1.response.SaleResponse;
import com.gabrielspassos.poc.dto.PersonDTO;
import com.gabrielspassos.poc.service.SaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class SaleController implements BaseVersion {

    private final SaleService saleService;
    private final ModelMapper modelMapper;

    @PostMapping("/sales")
    public Mono<SaleResponse> createSale(@RequestBody PersonRequest personRequest) {
        log.info("Iniciado a criação de venda, {}", personRequest);
        PersonDTO personDTO = modelMapper.map(personRequest, PersonDTO.class);
        return saleService.createNewSale(personDTO)
                .map(dto -> modelMapper.map(dto, SaleResponse.class))
                .doOnSuccess(response -> log.info("Criado venda {}", response));
    }

    @GetMapping("/sales/{id}")
    public Mono<SaleResponse> getSaleById(@PathVariable("id") String id) {
        log.info("Iniciado a consulta de venda {}", id);
        return saleService.getSaleById(id)
                .map(dto -> modelMapper.map(dto, SaleResponse.class))
                .doOnSuccess(response -> log.info("Localizado venda {}", response));
    }
}
