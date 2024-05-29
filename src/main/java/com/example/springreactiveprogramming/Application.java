package com.example.springreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class Application {

    // sends 0 to N items
    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .log(); // might come from db or remote service call
    }

    // send 1 or empty item
    public Mono<String> nameMono() {
        return Mono.just("hello")
                .log();
    }

    // Map operator
    public Flux<String> namesFluxMap() {
        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .map(String::toUpperCase)
                .log(); // might come from db or remote service call
    }

    // Filter operator
    public Flux<String> namesFluxFilter(int len) {
        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > len)
                .log(); // might come from db or remote service call
    }

    // FlatMap operator
    public Flux<String> namesFluxFlatMap(int len) {
        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > len)
                .flatMap(this::splitString)
                .log(); // might come from db or remote service call
    }

    public Flux<String> splitString(String name) {
        String[] split = name.split("");
        return Flux.fromArray(split);
    }

    public Flux<String> namesFluxFlatMapAsync(int len) {
        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > len)
                .flatMap(this::splitStringDelay)
                .log(); // might come from db or remote service call
    }

    public Flux<String> namesFluxTransform(int len) {
        Function<Flux<String>,Flux<String>> filterMap = name -> name
                .map(String::toUpperCase)
                .filter(s -> s.length() > len);

        return Flux.fromIterable(List.of("hello", "world", "nice", "day"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .log(); // might come from db or remote service call
    }

    public Flux<String> splitStringDelay(String name) {
        String[] split = name.split("");
        int i = new Random().nextInt(1000);
        return Flux.fromArray(split)
                .delayElements(Duration.ofMillis(i));
    }

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        Application application = new Application();

        application.namesFlux()
                .subscribe(log::info);

        application.nameMono()
                .subscribe(log::info);
    }
}

// 1. Reactive Streams are immutable not unless the methods are chained together
// 2. Concat map is similar to flatmap. The only difference is it maintains the seq ordering.
// Flatmap for mono
// FlatMapMany
// Transform - use to transform from one type to another