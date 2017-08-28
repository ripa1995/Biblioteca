package it.uninsubria.studenti.rripamonti.biblioteca.rest;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rober on 08/07/2017.
 */

public class BookCoverService {
    private static final String baseUrl = "http://webservices.amazon.com";

    private static BookCoverService instance;

    private BookCoverRestService restInterface;

    private BookCoverService(Context context){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restInterface = retrofit.
                create(BookCoverRestService.class);


    }

    public static synchronized BookCoverService getInstance(Context context){
        if (instance == null){
            instance = new BookCoverService(context);
        }
        return instance;
    }

    public void getBookCover(String asin, final Callback callback) {
        restInterface.getBookCoverInfo(asin).enqueue(new retrofit2.Callback<BookCoverContainer>(){
            @Override
            public void onResponse(Call<BookCoverContainer> call, Response<BookCoverContainer> response) {
                BookCoverContainer book = response.body();
                callback.onLoad(book.getBookCover());
            }

            @Override
            public void onFailure(Call<BookCoverContainer> call, Throwable t) {
                callback.onFailure();
            }
        });

    }

    public static interface Callback {
        public void onLoad(BookCover bookCover);
        public void onFailure();
    }
}
