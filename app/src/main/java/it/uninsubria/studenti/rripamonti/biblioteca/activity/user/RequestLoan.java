package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.GregorianCalendar;
import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;

/**
 * Created by rober on 03/05/2017.
 * Request Loan consente agli utenti non bibliotecari di richiedere prestiti
 */

public class RequestLoan extends AppCompatActivity {
    private static final String TAG = "Request Loan";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private Query ref2;
    private LibraryObject lo;
    private Loan loan;
    private Button btn_loan;
    private TextView mItemTitle, mItemAuthor, mItemCategory, mItemISBN, success, failure;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_loan);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_loan = (Button) findViewById(R.id.btn_loan);
        final ImageView mItemImage = (ImageView) findViewById(R.id.item_image);
         mItemTitle = (TextView) findViewById(R.id.tv_title);
         mItemAuthor = (TextView) findViewById(R.id.tv_author);
         mItemCategory = (TextView) findViewById(R.id.tv_category);
         mItemISBN = (TextView) findViewById(R.id.tv_isbn);
        mItemISBN.setVisibility(View.VISIBLE);
          success = (TextView) findViewById(R.id.tv_success);
          failure = (TextView) findViewById(R.id.tv_failure);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            lo = (LibraryObject) extras.getSerializable("key");
        }
        switch (lo.getType().toString()) {
            //in base al tipo cambio icona
            case "BOOK":
                //immagine libro
                mItemISBN.setText(lo.getIsbn());

                Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+lo.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(mItemImage);

                break;
            case "FILM":
                mItemISBN.setText(lo.getIsbn());
                mItemImage.setImageResource(R.drawable.ic_action_movie);
                MovieService.getInstance(getApplicationContext()).getMovie(lo.getIsbn(), new MovieService.Callback() {
                    @Override
                    public void onLoad(Movie movie) {
                        if (movie != null) {
                            Picasso.with(getApplicationContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(mItemImage);
                        }
                    }
                    @Override
                    public void onFailure() {

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

        mItemTitle.setText(lo.getTitle());
        mItemAuthor.setText(lo.getAuthor());
        mItemCategory.setText(lo.getCategory());

        btn_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //query sul database, prendo i loans e li ordino per id oggetto
                ref2 = database.getReference("loans").orderByChild("loId").equalTo(lo.getId());
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()==null){
                            //non esiste un prestito con quell'id oggetto
                            insertLoan();
                        }else {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                //guardo se il prestito è stato richiesto dall'utente attivo o da un terzo
                                Loan loan1 = childSnapshot.getValue(Loan.class);
                                Log.d(TAG, String.valueOf(loan1.getIdLoan()));
                                if (loan1.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                    failure.setText(getString(R.string.loan_already_requested));
                                    failure.setVisibility(View.VISIBLE);
                                    btn_loan.setVisibility(View.GONE);
                                    //prestito già richiesto dall'utente corrente
                                    break;
                                } else {
                                    Query ref3 = database.getReference("loans/" + loan1.getIdLoan()).child("start").equalTo("true");
                                    ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d(TAG, dataSnapshot.toString());
                                            if (dataSnapshot.getValue()==null){
                                                failure.setText(getString(R.string.loan_already_requested_another_user));
                                                failure.setVisibility(View.VISIBLE);
                                                btn_loan.setVisibility(View.GONE);
                                            }else {
                                                //oggetto in prestito da un altro utente
                                                Log.d(TAG, dataSnapshot.getValue().toString());
                                                failure.setText(getString(R.string.already_loaned_object));
                                                failure.setVisibility(View.VISIBLE);
                                                btn_loan.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    /*
    Crea un nuovo Loan e lo inserisce nel database,
    il prestito può essere richiesto solo se non vi sono prestiti attivi per quell'oggetto,
    il prestito non viene richiesto se è già stata fatta richiesta da qualche utente.
     */
    private void insertLoan() {
        String id = String.valueOf((new GregorianCalendar()).getTimeInMillis());
        loan = new Loan(lo.getId(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), id, lo.getType(), lo.getTitle(), lo.getIsbn(), lo.getAuthor());
        ref = database.getReference("loans");
        ref.child(id).setValue(loan);
        success.setVisibility(View.VISIBLE);
        btn_loan.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
}
