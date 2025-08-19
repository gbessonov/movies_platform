package io.github.gbessonov.movies_platform.movies.model;

import java.util.ArrayList;
import java.util.List;

public class MoviesListResponse implements MoviesResponse {
    private List<Movie> movies;

    public static MoviesListResponse fromList(List<Movie> movies) {
        return new MoviesListResponse(movies);
    }

    protected MoviesListResponse(List<Movie> movies) {
        this.movies = movies != null ? movies : new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getSize() {
        return movies.size();
    }

}
