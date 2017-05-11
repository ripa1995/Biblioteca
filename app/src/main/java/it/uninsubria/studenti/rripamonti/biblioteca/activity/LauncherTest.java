package it.uninsubria.studenti.rripamonti.biblioteca.activity;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.ExtraActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

public class LauncherTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_test);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("objects");
        Log.d(String.valueOf(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE)," prova");
        try {
            Log.d(String.valueOf(getPackageManager().getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0 ).versionCode)," prova");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int error = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,error,1);
        dialog.show();
        LibraryObject libraryObject = new LibraryObject("titolo","autore","categoria", Type.BOOK,"12345");
        LibraryObject libraryObject2 = new LibraryObject("titolo2","autore2","categoria2", Type.FILM);
        TextView tv = (TextView) findViewById(R.id.textView2);
        myRef.child(String.valueOf((new GregorianCalendar()).getTimeInMillis())).setValue(libraryObject);
        myRef.child(String.valueOf((new GregorianCalendar()).getTimeInMillis())).setValue(libraryObject2);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LibraryObject lo = dataSnapshot.getValue(LibraryObject.class);
                Log.d("Launcher", lo.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef = database.getReference("activities");
        ExtraActivity ea = new ExtraActivity("titolo","nome", "cognome",new GregorianCalendar(2017,06,20).getTimeInMillis());
        myRef.child(String.valueOf((new GregorianCalendar()).getTimeInMillis())).setValue(ea);

    }
}
