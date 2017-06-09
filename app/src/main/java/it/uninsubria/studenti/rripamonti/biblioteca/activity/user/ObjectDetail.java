package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;

public class ObjectDetail extends AppCompatActivity {
    private LibraryObject lo;
    private static final String TAG = "Object Detail";
    private ImageView mItemImage;
    private TextView mItemTitle, mItemAuthor, mItemISBN, mItemCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initUi();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            lo = (LibraryObject) extras.getSerializable("key");
        }
         populateView();

    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
    private void initUi(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         mItemImage = (ImageView) findViewById(R.id.item_image);
         mItemTitle = (TextView) findViewById(R.id.tv_title);
         mItemAuthor = (TextView) findViewById(R.id.tv_author);
         mItemCategory = (TextView) findViewById(R.id.tv_category);
         mItemISBN = (TextView) findViewById(R.id.tv_isbn);
        mItemISBN.setVisibility(View.VISIBLE);
    }

    //popola la view in base all'oggetto passato tramite intent
    private void populateView(){
        switch (lo.getType().toString()) {
            case "BOOK":
                //immagine libro


                Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+lo.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(mItemImage);
                break;
            case "FILM":
                MovieService.getInstance(getApplicationContext()).getMovie(lo.getIsbn(), new MovieService.Callback() {
                    @Override
                    public void onLoad(Movie movie) {
                        if (movie != null) {
                            Picasso.with(getApplicationContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(mItemImage);
                        }
                    }
                    @Override
                    public void onFailure() {
                        mItemImage.setImageResource(R.drawable.ic_action_movie);
                    }
                });

                break;
            case "MUSIC":
                mItemImage.setImageResource(R.drawable.ic_action_music_1);
                mItemISBN.setVisibility(View.GONE);
                AlbumService.getInstance(getApplicationContext()).getAlbum(lo.getAuthor(), lo.getTitle(), new AlbumService.Callback() {
                    @Override
                    public void onLoad(Album album) {
                        if (album != null) {
                            List<Album.Image> list = album.getImages();
                            Log.d(TAG, album.getArtist());
                            for (Album.Image image : list) {
                                if (image.getSize().equals("medium")) {
                                    Picasso.with(getApplicationContext()).load(image.getText()).placeholder(R.drawable.ic_action_music_1).error(R.drawable.ic_action_music_1).into(mItemImage);
                                }
                            }

                        }
                    }
                    @Override
                    public void onFailure() {
                    }
                });

                break;
        }
        mItemISBN.setText(lo.getIsbn());
        mItemTitle.setText(lo.getTitle());
        mItemAuthor.setText(lo.getAuthor());
        mItemCategory.setText(lo.getCategory());
    }
}
