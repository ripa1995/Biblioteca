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

import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

/**
 * Created by rober on 03/05/2017.
 */

public class RequestLoan extends AppCompatActivity {
    private static final String TAG = "Request Loan";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private Query ref2;
    private LibraryObject lo;
    private Loan loan;
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
        Button btn_loan = (Button) findViewById(R.id.btn_loan);
        ImageView mItemImage = (ImageView) findViewById(R.id.item_image);
         mItemTitle = (TextView) findViewById(R.id.tv_title);
         mItemAuthor = (TextView) findViewById(R.id.tv_author);
         mItemCategory = (TextView) findViewById(R.id.tv_category);
         mItemISBN = (TextView) findViewById(R.id.tv_isbn);
          success = (TextView) findViewById(R.id.tv_success);
          failure = (TextView) findViewById(R.id.tv_failure);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            lo = (LibraryObject) extras.getSerializable("key");
        }
        switch (lo.getType().toString()) {
            case "BOOK":
                //immagine libro
                mItemImage.setImageResource(R.drawable.ic_action_book);
                mItemISBN.setText(lo.getIsbn());
                mItemISBN.setVisibility(View.VISIBLE);
                break;
            case "FILM":
                mItemImage.setImageResource(R.drawable.ic_action_movie);
                mItemISBN.setVisibility(View.GONE);
                break;
            case "MUSIC":
                mItemImage.setImageResource(R.drawable.ic_action_music_1);
                mItemISBN.setVisibility(View.GONE);
                break;
        }

        mItemTitle.setText(lo.getTitle());
        mItemAuthor.setText(lo.getAuthor());
        mItemCategory.setText(lo.getCategory());

        btn_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref2 = database.getReference("loans").orderByChild("libraryObjectId").equalTo(lo.getId());
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue()==null){
                            insertLoan();
                        }else {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                Loan loan1 = childSnapshot.getValue(Loan.class);
                                Log.d(TAG, String.valueOf(loan1.getIdLoan()));
                                if (loan1.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                    failure.setVisibility(View.VISIBLE);
                                    break;
                                } else {
                                    Query ref3 = database.getReference("loans/" + loan1.getIdLoan()).child("start");
                                    ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d(TAG, dataSnapshot.toString());
                                            if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                                                //oggetto in prestito
                                                Log.d(TAG, dataSnapshot.getValue().toString());
                                                failure.setVisibility(View.VISIBLE);
                                            } else {
                                                //oggetto non in prestito
                                                Log.d(TAG, "null");
                                                insertLoan();
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

    private void insertLoan() {
        String id = String.valueOf((new GregorianCalendar()).getTimeInMillis());
        loan = new Loan(lo.getId(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), id, lo.getType(), lo.getTitle());
        ref = database.getReference("loans");
        ref.child(id).setValue(loan);
        success.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
}
