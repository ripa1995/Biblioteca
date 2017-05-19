package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rober on 19/05/2017.
 */

public class MovieService {

    private static final String baseUrl
            = "http://www.omdbapi.com";

    private static MovieService instance;

    private MovieRestService restInterface;

    private MovieService(Context context){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restInterface = retrofit.
                create(MovieRestService.class);


    }

    public static synchronized MovieService getInstance(Context context){
        if (instance == null){
            instance = new MovieService(context);
        }
        return instance;
    }

    public void getMovie(String imdbId, final Callback callback) {
        restInterface.getMovie(imdbId).
                enqueue(new retrofit2.Callback<Movie>() {

                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response)  {
                        Movie movie = response.body();
                        callback.onLoad(movie);
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onFailure();
                    }
                });

    }

    public static interface Callback {
        public void onLoad(Movie movie);
        public void onFailure();
}


}