package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditActivity" ;
    private LibraryObject lo;
    private EditText et_author, et_title, et_editore, et_year, et_cdd, et_nIngresso;
    private Button btn_confirm;
    private String id;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("objects");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            lo = (LibraryObject) extras.getSerializable("key");
        }
        et_title = (EditText) findViewById(R.id.et_title);
        et_author = (EditText) findViewById(R.id.et_author);
        et_editore = (EditText) findViewById(R.id.et_category);

        et_cdd = (EditText) findViewById(R.id.et_isbn);
        et_nIngresso = (EditText) findViewById(R.id.et_nIngresso);
        et_year = (EditText) findViewById(R.id.et_year);
        btn_confirm = (Button) findViewById(R.id.btn_confirm) ;
        btn_confirm.setOnClickListener(this);
        id = lo.getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }

    private String capitalizeFirstLetter(String input){
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }

    private String entryNumberControl(String input){
        if (input.length()<4){
            Toast.makeText(getApplicationContext(), getString(R.string.entry_format), Toast.LENGTH_LONG).show();
            return null;
        }
        if (input.substring(0,4).equals("IIM ")) {
            return input;
        } else if (input.substring(0,4).equalsIgnoreCase("IIM ")) {
            return input.substring(0,4).toUpperCase() + input.substring(4);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.entry_format), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (et_title.length()!=0){
            String string = capitalizeFirstLetter(et_title.getText().toString());
            myRef.child(id).child("title").setValue(string);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_title) ,Toast.LENGTH_LONG);
            toast.show();
        }
        if (et_author.length()!=0){
            String string = capitalizeFirstLetter(et_author.getText().toString());
            myRef.child(id).child("author").setValue(string);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_author) ,Toast.LENGTH_LONG);
            toast.show();
        }
        if (et_editore.length()!=0) {
            myRef.child(id).child("category").setValue(et_editore.getText().toString());
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_publisher) ,Toast.LENGTH_LONG);
            toast.show();
        }
        if (et_cdd.length()!=0){
            myRef.child(id).child("isbn").setValue(et_cdd.getText().toString());
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_cdd) ,Toast.LENGTH_LONG);
            toast.show();
        }
        if (et_nIngresso.length()!=0) {
            String string = entryNumberControl(et_nIngresso.getText().toString());
            if (string!=null) {
                myRef.child(id).child("nIngresso").setValue(string);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_entry), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        if (et_year.length()!= 0) {
            myRef.child(id).child("year").setValue(et_year.getText().toString());
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.edit_year) ,Toast.LENGTH_LONG);
            toast.show();
        }
        et_year.setText("");
        et_nIngresso.setText("");
        et_cdd.setText("");
        et_editore.setText("");
        et_author.setText("");
        et_title.setText("");
    }
}
