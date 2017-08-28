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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
public class SearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "SearchActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("objects");
    private static ArrayList<LibraryObject> items = new ArrayList<LibraryObject>();
    private Button btn_search_title, btn_search_author, btn_search_genre, btn_search_entry;
    private EditText et_search;
    private FirebaseAuth mAuth;
    private String selected;



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
        btn_search_title = (Button) findViewById(R.id.btn_search_title);
        btn_search_title.setOnClickListener(this);
        btn_search_author = (Button) findViewById(R.id.btn_search_author);
        btn_search_author.setOnClickListener(this);
        btn_search_genre = (Button) findViewById(R.id.btn_search_genre);
        btn_search_genre.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        btn_search_entry = (Button) findViewById(R.id.btn_search_entry) ;
        btn_search_entry.setOnClickListener(this);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_cdd);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genre, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Admin");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                    btn_search_genre.setVisibility(View.GONE);
                    btn_search_author.setVisibility(View.GONE);
                    btn_search_title.setVisibility(View.GONE);
                    btn_search_entry.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    et_search.setHint(getString(R.string.insert_entry_here));
                } else {
                    btn_search_genre.setVisibility(View.VISIBLE);
                    btn_search_author.setVisibility(View.VISIBLE);
                    btn_search_title.setVisibility(View.VISIBLE);
                    btn_search_entry.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    et_search.setHint(getString(R.string.insert_title_author));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_search_title:
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
                break;
            case R.id.btn_search_author:
                et_search.setError(null);
                String autore = et_search.getText().toString();

                if (autore.length()==0){
                    et_search.setError(getString(R.string.insert_author_here));
                } else {
                    Query ref = myRef.orderByChild("author").startAt(autore).endAt(autore+"\uf8ff");
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
                break;
            case R.id.btn_search_genre:
                    if (selected == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.select_genre),Toast.LENGTH_LONG).show();
                    } else {
                        Query ref = myRef.orderByChild("isbn").startAt(selected).endAt(selected+"\uf8ff");
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

                                        Intent intent = new Intent(v.getContext(),ObjectDetail.class);
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

                break;
            case R.id.btn_search_entry:
                et_search.setError(null);
                String entry = et_search.getText().toString();

                if (entry.length()==0){
                    et_search.setError(getString(R.string.insert_entry_number));
                } else {
                    Query ref = myRef.orderByChild("nIngresso").startAt("IIM "+entry).endAt("IIM "+entry+"\uf8ff");
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

                                    Intent intent = new Intent(v.getContext(),ObjectDetail.class);
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
                break;

        }



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                selected = null;
                break;
            case 1:
                selected = "R";
                break;
            case 2:
                selected="R A";
                break;
            case 3:
                selected = "R B";
                break;
            case 4:
                selected = "R C";
                break;
            case 5:
                selected = "R D";
                break;
            case 6:
                selected = "R E";
                break;
            case 7:
                selected = "R G";
                break;
            case 8:
                selected = "R H";
                break;
            case 9:
                selected = "R I";
                break;
            case 10:
                selected = "R K";
                break;
            case 11:
                selected = "R L";
                break;
            case 12:
                selected = "R M";
                break;
            case 13:
                selected = "R N";
                break;
            case 14:
                selected = "R P";
                break;
            case 15:
                selected = "R O";
                break;
            case 16:
                selected = "R F";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
