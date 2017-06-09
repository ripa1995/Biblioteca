package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LibraryObjectHolder;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;
/*
l'utente può effettuare delle ricerche nel catalogo, in base al titolo, la ricerca è case sensitive e supporta i prefissi.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("objects");
    private static ArrayList<LibraryObject> items = new ArrayList<LibraryObject>();
    private Button btn_search;
    private EditText et_search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myRef.keepSynced(true);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);

    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        //ricerca nel db
        et_search.setError(null);
        String titolo = et_search.getText().toString();

        if (titolo.length()==0){
            et_search.setError(getString(R.string.insert_title_error));
        } else {
            Query ref = myRef.orderByChild("title").startAt(titolo).endAt(titolo+"\uf8ff");
            adapter = new FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder>(LibraryObject.class, R.layout.recyclerview_item_row, LibraryObjectHolder.class, ref) {
                @Override
                protected void populateViewHolder(final LibraryObjectHolder viewHolder, LibraryObject model, final int position) {
                    switch (model.getType().toString()) {
                        case "BOOK":
                            //immagine libro
                            Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+model.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(viewHolder.mItemImage);
                            break;
                        case "FILM":
                            viewHolder.mItemImage.setImageResource(R.drawable.ic_action_movie);

                            MovieService.getInstance(getApplicationContext()).getMovie(model.getIsbn(), new MovieService.Callback() {
                                @Override
                                public void onLoad(Movie movie) {
                                    if (movie != null) {
                                        Picasso.with(getApplicationContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(viewHolder.mItemImage);
                                    }
                                }

                                @Override
                                public void onFailure() {
                                }
                            });

                            break;
                        case "MUSIC":
                            viewHolder.mItemImage.setImageResource(R.drawable.ic_action_music_1);
                            AlbumService.getInstance(getApplicationContext()).getAlbum(model.getAuthor(), model.getTitle(), new AlbumService.Callback() {
                                @Override
                                public void onLoad(Album album) {
                                    if (album!=null) {
                                        List<Album.Image> list = album.getImages();
                                        Log.d(TAG, album.getArtist());
                                        for (Album.Image image : list) {
                                            if (image.getSize().equals("medium")) {
                                                Picasso.with(getApplicationContext()).load(image.getText()).placeholder(R.drawable.ic_action_music_1).error(R.drawable.ic_action_music_1).into(viewHolder.mItemImage);
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
                    items.add(position, model);
                    viewHolder.mItemTitle.setText(model.getTitle());
                    viewHolder.mItemAuthor.setText(model.getAuthor());

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LibraryObject lo = items.get(position);

                            Intent intent = new Intent(v.getContext(),RequestLoan.class);
                            Bundle extras = new Bundle();
                            extras.putSerializable("key",lo);
                            intent.putExtras(extras);
                            v.getContext().startActivity(intent);
                        }
                    });
                }

            };
            recyclerView.setAdapter(adapter);
        }


    }


}
