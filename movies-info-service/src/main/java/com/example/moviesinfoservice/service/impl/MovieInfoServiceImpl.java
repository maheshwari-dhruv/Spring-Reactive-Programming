package com.example.moviesinfoservice.service.impl;

import com.example.moviesinfoservice.domain.MovieInfo;
import com.example.moviesinfoservice.repository.MovieInfoRepository;
import com.example.moviesinfoservice.service.MovieInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MovieInfoServiceImpl implements MovieInfoService {

    private MovieInfoRepository movieInfoRepository;

    public MovieInfoServiceImpl(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    @Override
    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    @Override
    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoRepository.findAll();
    }

    @Override
    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    @Override
    public Mono<MovieInfo> updateMovieInfo(MovieInfo movieInfo, String id) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo1 -> {
                    movieInfo1.setCast(movieInfo.getCast());
                    movieInfo1.setYear(movieInfo.getYear());
                    movieInfo1.setTitle(movieInfo.getTitle());
                    movieInfo1.setReleaseDate(movieInfo.getReleaseDate());

                    return movieInfoRepository.save(movieInfo1);
                });
    }

    @Override
    public Mono<Void> deleteMovieInfo(String id) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo1 -> movieInfoRepository.deleteById(id));
    }
}
