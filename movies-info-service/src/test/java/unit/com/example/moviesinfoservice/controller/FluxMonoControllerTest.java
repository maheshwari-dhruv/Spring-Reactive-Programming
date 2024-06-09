package com.example.moviesinfoservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxMonoController.class)
@AutoConfigureWebTestClient
class FluxMonoControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void fluxTest() {
        webClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .hasSize(3);
    }

    @Test
    void fluxTest_approach2() {
        Flux<Integer> responseBody = webClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier
                .create(responseBody)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    void fluxTest_approach3() {
        webClient
                .get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<Integer> responseBody = listEntityExchangeResult.getResponseBody();
                    assertEquals(3, Objects.requireNonNull(responseBody).size());
                });
    }

    @Test
    void monoTest() {
        webClient
                .get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals("Hello World", responseBody);
                });
    }

    @Test
    void stream() {
        Flux<Long> responseBody = webClient
                .get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier
                .create(responseBody)
                .expectNext(0L, 1L, 2L)
                .thenCancel()
                .verify();
    }
}