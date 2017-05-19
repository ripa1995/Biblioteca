package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LoanStatusHolder;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;

public class LoanStatusActivity extends AppCompatActivity {
    private static final String TAG = "LoanStatusActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Loan, LoanStatusHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("loans");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_status);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.loanstatus_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        Query myRef = ref.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        myRef.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<Loan, LoanStatusHolder>(Loan.class, R.layout.recyclerview_item_row_loanstatus, LoanStatusHolder.class, myRef) {
            @Override
            protected void populateViewHolder(final LoanStatusHolder viewHolder, Loan model, int position) {
                switch (model.getTipo().toString()) {
                    case "BOOK":
                        //immagine libro

                        Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+model.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(viewHolder.itemImage);

                        break;
                    case "FILM":
                        viewHolder.itemImage.setImageResource(R.drawable.ic_action_movie);
                        MovieService.getInstance(getApplicationContext()).getMovie(model.getIsbn(), new MovieService.Callback() {
                            @Override
                            public void onLoad(Movie movie) {
                                if (movie != null) {
                                    Picasso.with(getApplicationContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(viewHolder.itemImage);
                                }
                            }
                            @Override
                            public void onFailure() {

                            }
                        });
                        break;
                    case "MUSIC":
                        viewHolder.itemImage.setImageResource(R.drawable.ic_action_music_1);
                        AlbumService.getInstance(getApplicationContext()).getAlbum(model.getAuthor(), model.getTitle(), new AlbumService.Callback() {
                            @Override
                            public void onLoad(Album album) {
                                if (album != null) {
                                    List<Album.Image> list = album.getImages();
                                    Log.d(TAG, album.getArtist());
                                    for (Album.Image image : list) {
                                        if (image.getSize().equals("medium")) {
                                            Picasso.with(getApplicationContext()).load(image.getText()).placeholder(R.drawable.ic_action_music_1).error(R.drawable.ic_action_music_1).into(viewHolder.itemImage);
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
                Log.d("prova", model.toString());
                viewHolder.tvTitle.setText(model.getTitle());
                Log.d(TAG, String.valueOf(model.isStart()));
                if (model.isStart()) {
                    String startDate = new SimpleDateFormat("dd/MM/yyyy").format(model.getStart_date());
                    viewHolder.tvStartOfLoan.setText(startDate);
                    GregorianCalendar endDate = new GregorianCalendar();
                    endDate.setTimeInMillis(model.getStart_date());

                    endDate.add(Calendar.MONTH, 1);
                    String endOfLoan = new SimpleDateFormat("dd/MM/yyyy").format(endDate.getTime());
                    viewHolder.tvEndOfLoan.setText(endOfLoan);
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    //se è passato un mese
                    if (endDate.getTimeInMillis() < gregorianCalendar.getTimeInMillis()) {
                        viewHolder.tvEndOfLoan.setTextColor(Color.RED);
                    }

                } else {
                    viewHolder.tvStartOfLoan.setText(getString(R.string.not_start));
                    viewHolder.tvEndOfLoan.setText(null);
                }



            }
        };
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
}
