package com.example.springreactiveprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {
    Application application = new Application();

    @Test
    void nameFluxTest() {
        // given

        // when
        Flux<String> stringFlux = application.namesFlux();

        // then
        StepVerifier.create(stringFlux)
//                .expectNext("hello", "world", "nice", "day") // check the items
                .expectNextCount(4) // check the item count
                .verifyComplete();

    }

    @Test
    void nameMonoTest() {
        // given
        // when
        Mono<String> stringMono = application.nameMono();

        // then
        StepVerifier.create(stringMono)
                .expectNext("hello")
                .verifyComplete();
    }

    // Map Test
    @Test
    void nameFluxTestMap() {
        // given

        // when
        Flux<String> stringFlux = application.namesFluxMap();

        // then
        StepVerifier.create(stringFlux)
                .expectNext("HELLO", "WORLD", "NICE", "DAY") // check the items
//                .expectNextCount(4) // check the item count
                .verifyComplete();

    }

    @Test
    void namesFluxTestFilter() {
        Flux<String> stringFlux = application.namesFluxFilter(3);

        StepVerifier.create(stringFlux)
                .expectNext("HELLO", "WORLD", "NICE") // check the items
//                .expectNextCount(4) // check the item count
                .verifyComplete();
    }

    @Test
    void namesFluxTestFlatMap() {
        Flux<String> stringFlux = application.namesFluxFlatMap(3);

        StepVerifier.create(stringFlux)
                .expectNext("H", "E", "L", "L", "O", "W","O","R","L","D", "N","I","C","E") // check the items
//                .expectNextCount(4) // check the item count
                .verifyComplete();
    }

    @Test
    void namesFluxTestTransform() {
        Flux<String> stringFlux = application.namesFluxTransform(3);

        StepVerifier.create(stringFlux)
                .expectNext("H", "E", "L", "L", "O", "W","O","R","L","D", "N","I","C","E") // check the items
//                .expectNextCount(4) // check the item count
                .verifyComplete();
    }

    @Test
    void splitStringTest() {
        Flux<String> stringFlux = application.splitString("hello");

        StepVerifier.create(stringFlux)
                .expectNext("h", "e", "l", "l", "o") // check the items
//                .expectNextCount(4) // check the item count
                .verifyComplete();
    }

    @Test
    void namesFluxTestFlatMapAsync() {
        Flux<String> stringFlux = application.namesFluxFlatMapAsync(3);

        StepVerifier.create(stringFlux)
//                .expectNext("H", "E", "L", "L", "O", "W","O","R","L","D", "N","I","C","E") // check the items
                .expectNextCount(14) // check the item count
                .verifyComplete();
    }

    @Test
    void splitStringDelayTest() {
        Flux<String> stringFlux = application.splitStringDelay("hello");

        StepVerifier.create(stringFlux)
//                .expectNext("h", "e", "l", "l", "o") // check the items
                .expectNextCount(5) // check the item count
                .verifyComplete();
    }
}