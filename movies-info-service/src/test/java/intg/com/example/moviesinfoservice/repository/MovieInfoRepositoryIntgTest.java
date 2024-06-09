package com.example.moviesinfoservice.repository;

import com.example.moviesinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {
    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        List<MovieInfo> movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        "2005", List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        "2008", List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        "2012", List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieInfos)
                .blockLast(); // only allowed in test cases
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        Flux<MovieInfo> allMovies = movieInfoRepository.findAll().log();

        StepVerifier.create(allMovies)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findAllId() {
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc").log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        MovieInfo saveMovieInfo = new MovieInfo(null, "Batman Begins 1",
                "2005", List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2005-06-15"));
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(saveMovieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieId());
                    assertEquals("Batman Begins 1", movieInfo.getTitle());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        MovieInfo abc = movieInfoRepository.findById("abc").block();
        abc.setYear("2021");

        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(abc).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("2021", movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        movieInfoRepository.deleteById("abc").block();
        Flux<MovieInfo> allMovies = movieInfoRepository.findAll().log();

        StepVerifier.create(allMovies)
                .expectNextCount(2)
                .verifyComplete();
    }
}