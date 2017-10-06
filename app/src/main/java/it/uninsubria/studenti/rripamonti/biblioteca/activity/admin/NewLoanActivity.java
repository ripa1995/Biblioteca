package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;

public class NewLoanActivity extends AppCompatActivity {
    private Button btn_loan;
    private static final String TAG = "Request Loan";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private Query ref2;
    private LibraryObject lo;
    private Loan loan;
    private String email;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView success, failure;
    private EditText et_name, et_surname, et_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_loan);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            lo = (LibraryObject) extras.getSerializable("key");
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_loan = (Button) findViewById(R.id.btn_loan);
        success = (TextView) findViewById(R.id.tv_success);
        failure = (TextView) findViewById(R.id.tv_failure);
        et_class = (EditText) findViewById(R.id.et_class);
        et_name = (EditText) findViewById(R.id.et_name);
        et_surname = (EditText) findViewById(R.id.et_surname);

        btn_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNotEmpty()) {
                    email = et_name.getText().toString() + "." + et_surname.getText().toString() +"."+ et_class.getText().toString()+"@mail.com";
                    ref2 = database.getReference("loans").orderByChild("loId").equalTo(lo.getId());
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() == null) {
                                insertLoan();
                            } else {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Loan loan1 = childSnapshot.getValue(Loan.class);
                                    Log.d(TAG, String.valueOf(loan1.getIdLoan()));
                                    if (loan1.getUserId().equals(email)) {
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
                                                if (dataSnapshot.getValue() == null) {
                                                    failure.setText(getString(R.string.loan_already_requested_another_user));
                                                    failure.setVisibility(View.VISIBLE);
                                                    btn_loan.setVisibility(View.GONE);
                                                } else {
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
            }
        });

    }


    private boolean editTextNotEmpty() {
        if (et_name.length()==0 || et_surname.length()==0 || et_class.length() ==0){
            Toast.makeText(getApplicationContext(), getString(R.string.fill_field), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /*
    Crea un nuovo Loan e lo inserisce nel database,
    il prestito può essere richiesto solo se non vi sono prestiti attivi per quell'oggetto,
    il prestito non viene richiesto se è già stata fatta richiesta da qualche utente.
     */
    private void insertLoan() {
        String id = String.valueOf((new GregorianCalendar()).getTimeInMillis());
        loan = new Loan(lo.getId(), email, id, lo.getType(), lo.getTitle(), lo.getIsbn(), lo.getAuthor());
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
