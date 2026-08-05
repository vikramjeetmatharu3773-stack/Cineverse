package com.cineverse.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @GET("api/movies")
    Call<List<Movie>> getMovies();

    @GET("api/movies/{id}")
    Call<Movie> getMovie(@Path("id") int id);

    @POST("api/movies")
    Call<Movie> addMovie(Movie movie);
}