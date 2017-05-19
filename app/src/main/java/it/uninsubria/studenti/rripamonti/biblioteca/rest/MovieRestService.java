package it.uninsubria.studenti.rripamonti.biblioteca.rest;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rober on 19/05/2017.
 */

public interface MovieRestService {
    @GET("/?")
    public Call<Movie> getMovie(@Query("i") String imdbId);
}
