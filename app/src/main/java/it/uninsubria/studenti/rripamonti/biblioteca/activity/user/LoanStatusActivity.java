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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Loan;

public class LoanStatusActivity extends AppCompatActivity {
    private static final String TAG = "LoanStatusActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Loan, LoanItemHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("loans");
    public static class LoanItemHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private TextView tvStartOfLoan;
        private TextView tvEndOfLoan;
        private TextView tvTitle;
        private LibraryObject mLibraryItem = new LibraryObject();

        public LoanItemHolder(View v) {
            super(v);
            itemImage = (ImageView) v.findViewById(R.id.item_image);
            tvTitle = (TextView) v.findViewById(R.id.item_title);
            tvStartOfLoan = (TextView) v.findViewById(R.id.item_dateOfLoan);
            tvEndOfLoan = (TextView) v.findViewById(R.id.item_endOfLoan);

        }
    }

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

        adapter = new FirebaseRecyclerAdapter<Loan, LoanItemHolder>(Loan.class, R.layout.recyclerview_item_row_loanstatus, LoanItemHolder.class, myRef) {
            @Override
            protected void populateViewHolder(LoanItemHolder viewHolder, Loan model, int position) {
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
