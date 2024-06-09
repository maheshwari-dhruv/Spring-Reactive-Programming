package com.example.moviesinfoservice.controller;

import com.example.moviesinfoservice.domain.MovieInfo;
import com.example.moviesinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerIntgTest {
    public static final String POST_PATH = "/v1/movieinfos";

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    private WebTestClient webTestClient;

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
    void addMovieInfo() {
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins 1",
                "2005", List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient
                .post()
                .uri(POST_PATH)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    assertNotNull(responseBody.getMovieId());
                });
    }

    @Test
    void getMovieInfo() {
        webTestClient
                .get()
                .uri(POST_PATH)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void getMovieInfoById() {
        String id = "abc";

        webTestClient
                .get()
                .uri(POST_PATH + "/{id}", id)
                .exchange()
                .expectStatus()
                .isFound()
                .expectBody()
                .jsonPath("$.year").isEqualTo("2012");
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(responseBody);
//                    assertEquals("2012", responseBody.getYear());
//                });
    }

    @Test
    void updateMovieInfo() {
        String id = "abc";
        MovieInfo movieInfo = new MovieInfo(null, "Dark Knight Rises 1",
                "2005", List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient
                .put()
                .uri(POST_PATH + "/{id}", id)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals("Dark Knight Rises 1", responseBody.getTitle());
                });
    }

    @Test
    void deleteMovieInfoById() {
        String id = "abc";

        webTestClient
                .delete()
                .uri(POST_PATH + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}