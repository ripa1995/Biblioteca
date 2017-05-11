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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;

/**
 * Created by rober on 03/05/2017.
 */

public class RequestLoan extends AppCompatActivity {
    private static final String TAG = "Request Loan";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private LibraryObject lo;
    private Loan loan;
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
        TextView mItemTitle = (TextView) findViewById(R.id.tv_title);
        TextView mItemAuthor = (TextView) findViewById(R.id.tv_author);
        TextView mItemCategory = (TextView) findViewById(R.id.tv_category);
        TextView mItemISBN = (TextView) findViewById(R.id.tv_isbn);
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
                loan = new Loan(lo);
                ref = database.getReference("users");
                ref.child(firebaseAuth.getInstance().getCurrentUser().getUid()).child("loans").child(String.valueOf((new GregorianCalendar()).getTimeInMillis())).setValue(loan);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
}
