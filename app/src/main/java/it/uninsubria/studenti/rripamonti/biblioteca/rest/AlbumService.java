package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rober on 19/05/2017.
 */

public class AlbumService {
    private static final String baseUrl = "http://ws.audioscrobbler.com";

    private static AlbumService instance;

    private AlbumRestService restInterface;

    private AlbumService(Context context){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restInterface = retrofit.
                create(AlbumRestService.class);


    }

    public static synchronized AlbumService getInstance(Context context){
        if (instance == null){
            instance = new AlbumService(context);
        }
        return instance;
    }

    public void getAlbum(String artist, String mAlbum, final Callback callback) {
        restInterface.getAlbumInfo(artist,mAlbum).
                enqueue(new retrofit2.Callback<AlbumContainer>() {

                    @Override
                    public void onResponse(Call<AlbumContainer> call, Response<AlbumContainer> response)  {
                        AlbumContainer album = response.body();
                        callback.onLoad(album.getAlbum());
                    }

                    @Override
                    public void onFailure(Call<AlbumContainer> call, Throwable t) {
                        callback.onFailure();
                    }
                });

    }

    public static interface Callback {
        public void onLoad(Album album);
        public void onFailure();
    }


}
