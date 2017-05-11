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

import com.google.firebase.auth.FirebaseAuth;

import it.uninsubria.studenti.rripamonti.biblioteca.activity.FirebaseLoginActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.R;

public class AdminMainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private static final String TAG ="AdminMainActivity";
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        myToolbar.setOnMenuItemClickListener(this);



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
