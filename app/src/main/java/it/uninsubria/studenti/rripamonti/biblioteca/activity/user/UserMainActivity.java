package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import it.uninsubria.studenti.rripamonti.biblioteca.activity.LoginActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Item;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Model;
import it.uninsubria.studenti.rripamonti.biblioteca.model.recycler.UserRecyclerAdapter;

public class UserMainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{
    private static final String TAG = "UserMainActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Object> items = new ArrayList<Object>();
    private ArrayAdapter<Object> arrayAdapter;
    private Model model = Model.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        myToolbar.setOnMenuItemClickListener(this);
        items.clear();
        items.addAll(model.getLastAddedObject());
        recyclerView = (RecyclerView) findViewById(R.id.userRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new UserRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        items.clear();
        items.addAll(model.getLastAddedObject());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(TAG,"action LOGOUT has clicked");
                model.logOut();
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_settings:
                Log.d(TAG,"action SETTING has clicked");
                return true;
            case R.id.action_help:
                Log.d(TAG,"action HELP has clicked");
                return true;
            case R.id.action_loan_status:
                Log.d(TAG,"action LOANSTATUS has clicked");
                intent = new Intent(getApplicationContext(), LoanStatusActivity.class);
                startActivity(intent);
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
