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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.admin.EditActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;

public class ObjectDetail extends AppCompatActivity implements View.OnClickListener {
    private LibraryObject lo;
    private static final String TAG = "Object Detail";
    private ImageView mItemImage;
    private TextView mItemTitle, mItemAuthor, mItemISBN, mItemCategory;
    private Button btn_edit;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initUi();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Admin");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.getValue().toString());
                if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                    btn_edit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setVisibility(View.GONE);
        btn_edit.setOnClickListener(this);
    }

    //popola la view in base all'oggetto passato tramite intent
    private void populateView(){
        switch (lo.getType().toString()) {
            case "BOOK":
                //immagine libro

                mItemISBN.setText(lo.getIsbn());
                Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+lo.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(mItemImage);
                break;
            case "FILM":
                mItemISBN.setText(lo.getIsbn());
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

        mItemTitle.setText(lo.getTitle());
        mItemAuthor.setText(lo.getAuthor());
        mItemCategory.setText(lo.getCategory());
    }

    @Override
    public void onClick(View v) {
        if (lo.getType().equals(Type.BOOK)){
            Intent intent = new Intent(v.getContext(),EditActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("key",lo);
            intent.putExtras(extras);
            v.getContext().startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.only_book),Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
