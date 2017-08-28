package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.activity.FirebaseLoginActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.SearchActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.CSVTools;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LoanHolder;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;
/*
schermata principale admin, mostra le richieste di prestito, da qui un admin può far partire un prestito
 */
public class AdminMainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG ="AdminMainActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Loan, LoanHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("loans");
    private static ArrayList<Loan> items = new ArrayList<Loan>();
    private final static int NOT_LOANABLE = 0;
    private AlertDialog.Builder alertDialogBuilder;
    private List<LibraryObject> libri = new LinkedList<LibraryObject>();
    private List<LibraryObject> film = new LinkedList<LibraryObject>();
    private List<LibraryObject> musica = new LinkedList<LibraryObject>();
    private long count = -1;
    private long sum = 0;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        myToolbar.setOnMenuItemClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.adminRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference databaseReference = database.getReference("objects");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    count = dataSnapshot.getChildrenCount();
                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
                        LibraryObject obj = new LibraryObject();
                        for (DataSnapshot snap2 : snap.getChildren()) {

                            Log.d(TAG, snap2.toString());
                            String key = snap2.getKey();
                            switch (key){
                                case "author":
                                    obj.setAuthor(snap2.getValue().toString());
                                    break;
                                case "category":
                                    obj.setCategory(snap2.getValue().toString());
                                    break;
                                case "id":
                                    obj.setId(snap2.getValue().toString());
                                    break;
                                case "isbn":
                                    obj.setIsbn(snap2.getValue().toString());
                                    break;
                                case "title":
                                    obj.setTitle(snap2.getValue().toString());
                                    break;
                                case "type":
                                    obj.setType(Type.valueOf(snap2.getValue().toString()));
                                    break;
                                case "year":
                                    obj.setYear(snap2.getValue().toString());
                                    break;
                                case "nIngresso":
                                    obj.setnIngresso(snap2.getValue().toString());
                                    break;
                            }

                        }
                        switch (obj.getType().toString()) {
                            case "BOOK":
                                libri.add(obj);
                                break;
                            case "FILM":
                                film.add(obj);
                                break;
                            case "MUSIC":
                                musica.add(obj);
                                break;
                        }
                        sum = libri.size() + musica.size() + film.size();
                        Log.d("Thread", "somma"+ String.valueOf(sum));
                    }

                    Log.d(TAG, String.valueOf(count));

                } else {
                    count = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query ref = myRef.orderByChild("start").equalTo(false);

        adapter = new FirebaseRecyclerAdapter<Loan, LoanHolder>(Loan.class, R.layout.recyclerview_item_row, LoanHolder.class, ref) {
            @Override
            protected void populateViewHolder(final LoanHolder viewHolder, final Loan model, final int position) {
                switch (model.getTipo().toString()) {
                    case "BOOK":
                        //immagine libro

                        Picasso.with(getApplicationContext()).load("http://covers.openlibrary.org/b/isbn/"+model.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(viewHolder.mItemImage);

                        break;
                    case "FILM":
                        MovieService.getInstance(getApplicationContext()).getMovie(model.getIsbn(), new MovieService.Callback() {
                            @Override
                            public void onLoad(Movie movie) {
                                if (movie!=null) {
                                    Picasso.with(getApplicationContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(viewHolder.mItemImage);
                                }
                            }

                            @Override
                            public void onFailure() {
                                viewHolder.mItemImage.setImageResource(R.drawable.ic_action_movie);
                            }
                        });

                        break;
                    case "MUSIC":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_music_1);
                        AlbumService.getInstance(getApplicationContext()).getAlbum(model.getAuthor(), model.getTitle(), new AlbumService.Callback() {
                            @Override
                            public void onLoad(Album album) {
                                if (album != null) {
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
                viewHolder.mItemAuthor.setText(model.getUserId());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View view = v;
                        alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                        alertDialogBuilder.setMessage(getString(R.string.dialog_start_loan));
                        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final Query query = database.getReference("loans").orderByChild("loId").equalTo(model.getLoId());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount()==1){
                                            Log.d(TAG,dataSnapshot.toString());
                                            database.getReference().child("loans").child(model.getIdLoan()).child("start").setValue(true);
                                            database.getReference().child("loans").child(model.getIdLoan()).child("start_date").setValue(new GregorianCalendar().getTimeInMillis());
                                        }
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            Loan loan1 = childSnapshot.getValue(Loan.class);
                                            if (!loan1.getIdLoan().equals(model.getIdLoan())) {
                                                Query query1 = database.getReference("loans/" + loan1.getIdLoan()).child("start");
                                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    int counter = 0;

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue()==null){
                                                            database.getReference().child("loans").child(model.getIdLoan()).child("start").setValue(true);
                                                            database.getReference().child("loans").child(model.getIdLoan()).child("start_date").setValue(new GregorianCalendar().getTimeInMillis());
                                                        } else if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                                                            //oggetto in prestito
                                                            counter++;
                                                            Log.d(TAG, counter + "");
                                                            Log.d(TAG, dataSnapshot.getValue().toString());
                                                            showSnackbar(view,NOT_LOANABLE);
                                                        } else if (counter == 0) {
                                                            database.getReference().child("loans").child(model.getIdLoan()).child("start").setValue(true);
                                                            database.getReference().child("loans").child(model.getIdLoan()).child("start_date").setValue(new GregorianCalendar().getTimeInMillis());

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);




    }

    public void showSnackbar(View view, int code){
        switch (code){
            case NOT_LOANABLE:
                Snackbar.make(view, getString(R.string.already_loaned_object), Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(TAG,"action LOGOUT has clicked");
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getApplicationContext(), FirebaseLoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_endLoan:
                intent = new Intent(getApplicationContext(), EndOfLoan.class);
                startActivity(intent);
                return true;


            case R.id.action_add:
                Log.d(TAG,"action ADD has clicked");
                intent = new Intent(getApplicationContext(), AddObjectActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_save:
                /*List<LibraryObject> list = CSVTools.leggiCSVlibri();
                Iterator<LibraryObject> iterator = list.iterator();
                while (iterator.hasNext()){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String id = String.valueOf(new GregorianCalendar().getTimeInMillis());
                    LibraryObject libraryObject = iterator.next();
                    libraryObject.setId(id);
                    myRef = database.getReference("objects");
                    myRef.child(id).setValue(libraryObject);
                }
*/


                Log.d(TAG,"action save");
                if (count==0){
                    Log.d(TAG, "Niente da salvare");
                    Toast.makeText(getApplicationContext(), getString(R.string.nothing_save),Toast.LENGTH_LONG).show();
                }
                if (sum<count) {
                    Log.d(TAG, "aspetta");
                    Toast.makeText(getApplicationContext(), getString(R.string.wait_save),Toast.LENGTH_LONG).show();
                }
                if (sum == count) {
                    Log.d(TAG, "salva");
                    try {
                        CSVTools.salvaCSV(libri,film,musica);
                        Toast.makeText(getApplicationContext(), getString(R.string.success_save),Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                return true;

            case R.id.action_search:
                Log.d(TAG,"action SEARCH has clicked");
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;





        }
        return false;
    }
}
