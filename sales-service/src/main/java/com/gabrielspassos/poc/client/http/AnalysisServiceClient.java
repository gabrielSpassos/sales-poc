package com.gabrielspassos.poc.client.http;

import com.gabrielspassos.poc.client.http.response.ScoreResponse;
import com.gabrielspassos.poc.exception.NotFoundPersonException;
import com.gabrielspassos.poc.exception.UnexpectedInternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Slf4j
@Component
public class AnalysisServiceClient {

    private final String url;
    private final WebClient webClient;

    public AnalysisServiceClient(@Value("${client.analysis-service.url}") String url) {
        this.url = url;
        this.webClient = WebClient.builder().build();
    }

    public Mono<ScoreResponse> getPersonScore(String nationalIdentificationNumber) {
        String uri = createUri(nationalIdentificationNumber);
        log.info("Request GET: {}", uri);
        return webClient.get()
                .uri(uri)
                .headers(getHeaders())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new NotFoundPersonException()))
                .onStatus(HttpStatus::isError, response -> Mono.error(new UnexpectedInternalException()))
                .bodyToMono(ScoreResponse.class)
                .doOnSuccess(response -> log.info("GET {} | Response: {}", uri, response));
    }

    private String createUri(String nationalIdentificationNumber) {
        return UriComponentsBuilder
                .fromUriString(url)
                .pathSegment("people", nationalIdentificationNumber, "scores")
                .toUriString();
    }

    private Consumer<HttpHeaders> getHeaders() {
        return (httpHeaders -> {
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        });
    }

}
