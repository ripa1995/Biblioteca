package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.ExtraActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.enums.Type;

public class AddObjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG ="AddObjectActivity";
    private EditText tv_title, tv_author, tv_category, tv_name, tv_surname, tv_date, tv_isbn;
    private int selected = -1;
    private Button btn_confirm;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        tv_title = (EditText) findViewById(R.id.et_title);
        tv_author = (EditText) findViewById(R.id.et_author);
        tv_category = (EditText) findViewById(R.id.et_category);
        tv_name = (EditText) findViewById(R.id.et_name);
        tv_surname = (EditText) findViewById(R.id.et_surname);
        tv_date = (EditText) findViewById(R.id.et_date);
        tv_isbn = (EditText) findViewById(R.id.et_isbn);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);


    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (pos){
            case (0):
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.GONE);
                tv_category.setVisibility(View.GONE);
                tv_isbn.setVisibility(View.GONE);
                tv_name.setVisibility(View.VISIBLE);
                tv_surname.setVisibility(View.VISIBLE);
                tv_date.setVisibility(View.VISIBLE);
                selected = 0;
                break;
            case (1):
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
                tv_isbn.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                selected = 1;
                break;
            case (2):
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_isbn.setVisibility(View.GONE);
                tv_category.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                selected = 2;
                break;
            case (3):
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
                tv_isbn.setVisibility(View.GONE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                selected = 3;
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

        tv_title.setVisibility(View.GONE);
        tv_author.setVisibility(View.GONE);
        tv_category.setVisibility(View.GONE);
        tv_isbn.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_surname.setVisibility(View.GONE);
        tv_date.setVisibility(View.GONE);
        selected = -1;
    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (selected){
            case (0):
                //creare extraActivity
                if (tv_title.length()==0){
                    tv_title.setError("Insert title!");
                } else if (tv_name.length()==0){
                    tv_name.setError("Insert name!");
                } else if (tv_surname.length()==0){
                    tv_surname.setError("Insert surname!");
                } else {
                    // necessario per fare il parsing della data
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    df.setLenient(false);
                    GregorianCalendar date = new GregorianCalendar();
                    try {
                        date.setTime(df.parse(tv_date.getText().toString()));
                    } catch (ParseException e) {
                        tv_date.setError(getString(R.string.dateformat_error));
                    }
                    ExtraActivity ea = new ExtraActivity(tv_title.getText().toString(), tv_name.getText().toString(), tv_surname.getText().toString(), date.getTimeInMillis());
                    myRef = database.getReference("activities");
                    myRef.child(String.valueOf(new GregorianCalendar().getTimeInMillis())).setValue(ea);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_activity), Toast.LENGTH_LONG).show();
                }
                break;
            case(1):
                //creare libro
                if (tv_title.length()==0){
                    tv_title.setError("Insert title!");
                } else if (tv_author.length()==0){
                    tv_author.setError("Insert author!");
                } else if (tv_category.length()==0) {
                    tv_category.setError("Insert category!");
                }else if(tv_isbn.length()==0){
                    tv_isbn.setError("Inser ISBN!");
                } else{
                    LibraryObject libraryObject = new LibraryObject(tv_title.getText().toString(),tv_author.getText().toString(),tv_category.getText().toString(), Type.BOOK,tv_isbn.getText().toString());
                    myRef = database.getReference("objects");
                    myRef.child(String.valueOf(new GregorianCalendar().getTimeInMillis())).setValue(libraryObject);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_book), Toast.LENGTH_LONG).show();
                }


                break;
            case(2):
                //Creare music
                if (tv_title.length()==0){
                    tv_title.setError("Insert title!");
                } else if (tv_author.length()==0){
                    tv_author.setError("Insert author!");
                } else if (tv_category.length()==0) {
                    tv_category.setError("Insert category!");
                } else {
                    LibraryObject libraryObject = new LibraryObject(tv_title.getText().toString(),tv_author.getText().toString(),tv_category.getText().toString(), Type.MUSIC);
                    myRef = database.getReference("objects");
                    myRef.child(String.valueOf(new GregorianCalendar().getTimeInMillis())).setValue(libraryObject);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_music), Toast.LENGTH_LONG).show();
                }


                break;
            case(3):
                //creare film
                if (tv_title.length()==0){
                    tv_title.setError("Insert title!");
                } else if (tv_author.length()==0){
                    tv_author.setError("Insert author!");
                } else if (tv_category.length()==0) {
                    tv_category.setError("Insert category!");
                } else {
                    LibraryObject libraryObject = new LibraryObject(tv_title.getText().toString(), tv_author.getText().toString(), tv_category.getText().toString(), Type.FILM);
                    myRef = database.getReference("objects");
                    myRef.child(String.valueOf(new GregorianCalendar().getTimeInMillis())).setValue(libraryObject);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_film), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this, getString(R.string.no_type_selected), Toast.LENGTH_LONG).show();
        }
    }

    private void clearTextView() {
        tv_title.setText("");
        tv_author.setText("");
        tv_category.setText("");
        tv_isbn.setText("");
        tv_name.setText("");
        tv_surname.setText("");
        tv_date.setText("");
    }

    private void clearErrorTv(){
        tv_title.setError(null);
        tv_author.setError(null);
        tv_category.setError(null);
        tv_isbn.setError(null);
        tv_name.setError(null);
        tv_surname.setError(null);
        tv_date.setError(null);
    }
}
