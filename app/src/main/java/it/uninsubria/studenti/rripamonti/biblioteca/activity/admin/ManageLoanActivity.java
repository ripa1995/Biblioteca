package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.uninsubria.studenti.rripamonti.biblioteca.R;

public class ManageLoanActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG ="ManageLoanActivity";
    private Button btn_accept, btn_decline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_loan);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_decline = (Button) findViewById(R.id.btn_decline);
        btn_accept.setOnClickListener(this);
        btn_decline.setOnClickListener(this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btn_accept):
                //accetta prestito
                break;
            case (R.id.btn_decline):
                //rifiuta prestito
                break;
        }
    }
}
