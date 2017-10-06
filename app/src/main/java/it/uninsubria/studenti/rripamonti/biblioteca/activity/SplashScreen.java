package it.uninsubria.studenti.rripamonti.biblioteca.activity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.admin.AdminMainActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.UserTabbedActivity;


public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Log.d(TAG,firebaseAuth.toString());
        if (firebaseAuth.getCurrentUser()!=null){
            FirebaseUser user = firebaseAuth.getCurrentUser();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Admin");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d(TAG, dataSnapshot.getValue().toString());
                    if(Boolean.parseBoolean(dataSnapshot.getValue().toString())){
                        Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Intent intent = new Intent(getApplicationContext(), UserTabbedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
        Intent intent = new Intent(this.getApplicationContext(), FirebaseLoginActivity.class);
        startActivity(intent);
        finish();
        }

        AsyncTask<Void,Void,Boolean> asyncTask = new AsyncTask<Void,Void,Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isNetworkAvailable()) {
                    try {
                        //controllo se c'è connessione a internet
                        HttpURLConnection urlc = (HttpURLConnection)
                                (new URL("http://clients3.google.com/generate_204")
                                        .openConnection());
                        urlc.setRequestProperty("User-Agent", "Android");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        return (urlc.getResponseCode() == 204 &&
                                urlc.getContentLength() == 0);
                    } catch (IOException e) {
                        Log.e(TAG, "Error checking internet connection", e);
                    }
                } else {
                    Log.d(TAG, "No network available!");
                }
                return false;
            }
            private boolean isNetworkAvailable() {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null;
            }
            @Override
            protected void onPostExecute(final Boolean success) {

                if (!success) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.no_internet),Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };
        asyncTask.execute((Void)null);
    }



}