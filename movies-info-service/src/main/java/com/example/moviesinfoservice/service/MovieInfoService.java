package com.example.moviesinfoservice.service;

import com.example.moviesinfoservice.domain.MovieInfo;
import io.micrometer.common.util.internal.logging.InternalLogger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoService {
    Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo);

    Flux<MovieInfo> getAllMovieInfo();

    Mono<MovieInfo> getMovieInfoById(String id);

    Mono<MovieInfo> updateMovieInfo(MovieInfo movieInfo, String id);

    Mono<Void> deleteMovieInfo(String id);

    Flux<MovieInfo> getMovieInfoByYear(Integer year);

    Mono<MovieInfo> getMovieInfoByTitle(String title);
}
