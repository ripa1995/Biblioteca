package it.uninsubria.studenti.rripamonti.biblioteca.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uninsubria.studenti.rripamonti.biblioteca.activity.admin.AdminMainActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.UserTabbedActivity;


public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
        } else {
            Intent intent = new Intent(this.getApplicationContext(), FirebaseLoginActivity.class);
            startActivity(intent);
            finish();
        }
    }}