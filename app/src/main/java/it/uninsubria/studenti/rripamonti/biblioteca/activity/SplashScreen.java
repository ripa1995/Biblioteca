package it.uninsubria.studenti.rripamonti.biblioteca.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import it.uninsubria.studenti.rripamonti.biblioteca.model.DbHelper;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Model;


public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.setInstance(getApplicationContext());
        Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }}