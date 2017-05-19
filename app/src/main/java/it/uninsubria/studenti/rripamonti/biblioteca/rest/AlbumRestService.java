package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rober on 19/05/2017.
 */

public interface AlbumRestService {
    final public static String API_KEY = "fa1d6a5c5e6cffd4e33d42266d5e092e";

    @GET("/2.0/?method=album.getinfo&format=json&api_key=" + API_KEY)
    Call<AlbumContainer> getAlbumInfo(@Query("artist") String artist, @Query("album") String album);

}
