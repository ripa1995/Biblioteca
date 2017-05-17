package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LoanHolder;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LoanStatusHolder;

public class EndOfLoan extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Loan, LoanStatusHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("loans");
    private static ArrayList<Loan> items = new ArrayList<Loan>();
    private Button btn_search;
    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_loan);
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
        items.clear();
        et_search.setError(null);
        String id = et_search.getText().toString();

        if (id.length() == 0) {
            et_search.setError(getString(R.string.insert_userid));
        } else {
            Query ref = myRef.orderByChild("userId").startAt(id).endAt(id+"\uF8FF");

            adapter = new FirebaseRecyclerAdapter<Loan, LoanStatusHolder>(Loan.class, R.layout.recyclerview_item_row_loanstatus, LoanStatusHolder.class, ref) {
                @Override
                protected void populateViewHolder(LoanStatusHolder viewHolder, final Loan model, final int position) {
                    switch (model.getTipo().toString()) {
                        case "BOOK":
                            //immagine libro
                            viewHolder.itemImage.setImageResource(R.drawable.ic_action_book);

                            break;
                        case "FILM":
                            viewHolder.itemImage.setImageResource(R.drawable.ic_action_movie);

                            break;
                        case "MUSIC":
                            viewHolder.itemImage.setImageResource(R.drawable.ic_action_music_1);

                            break;
                    }
                    items.add(position, model);
                    viewHolder.tvTitle.setText(model.getTitle());
                    viewHolder.tvStartOfLoan.setText(model.getUserId());
                    if (model.isStart()){
                        viewHolder.tvEndOfLoan.setText(new SimpleDateFormat("dd/MM/yyyy").format(model.getStart_date()));
                    } else {
                        viewHolder.tvEndOfLoan.setText(getString(R.string.not_started));
                    }
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //creare un dialog per la conferma della cancellazione
                            database.getReference().child("loans").child(model.getIdLoan()).removeValue();
                        }
                    });

                }
            };
            recyclerView.setAdapter(adapter);
        }
    }
}
