package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.activity.FirebaseLoginActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LoanHolder;

public class AdminMainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG ="AdminMainActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Loan, LoanHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("loans");
    private static ArrayList<Loan> items = new ArrayList<Loan>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        myToolbar.setOnMenuItemClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.adminRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query ref = myRef.orderByChild("start").equalTo(false);

        adapter = new FirebaseRecyclerAdapter<Loan, LoanHolder>(Loan.class, R.layout.recyclerview_item_row, LoanHolder.class, ref) {
            @Override
            protected void populateViewHolder(LoanHolder viewHolder, final Loan model, final int position) {
                switch (model.getTipo().toString()) {
                    case "BOOK":
                        //immagine libro
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_book);

                        break;
                    case "FILM":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_movie);

                        break;
                    case "MUSIC":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_music_1);

                        break;
                }
                items.add(position, model);
                viewHolder.mItemTitle.setText(model.getTitle());
                viewHolder.mItemAuthor.setText(model.getUserId());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Query query = database.getReference("loans").orderByChild("libraryObjectId").equalTo(model.getLibraryObjectId());
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

                                                } else {
                                                    if (counter == 0) {
                                                        database.getReference().child("loans").child(model.getIdLoan()).child("start").setValue(true);
                                                        database.getReference().child("loans").child(model.getIdLoan()).child("start_date").setValue(new GregorianCalendar().getTimeInMillis());
                                                    }
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

            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
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
            case R.id.action_settings:
                Log.d(TAG,"action SETTING has clicked");


                return true;
            case R.id.action_endLoan:
                intent = new Intent(getApplicationContext(), EndOfLoan.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                Log.d(TAG,"action HELP has clicked");
                return true;
            case R.id.action_add:
                Log.d(TAG,"action ADD has clicked");
                intent = new Intent(getApplicationContext(), AddObjectActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
