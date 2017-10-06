package it.uninsubria.studenti.rripamonti.biblioteca.activity.admin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import static it.uninsubria.studenti.rripamonti.biblioteca.R.id.tv_isbn;

/*
permette all'admin di aggiungere oggetti al catalogo
 */
public class AddObjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG ="AddObjectActivity";
    private EditText tv_title, tv_author, tv_category, tv_name, tv_surname, tv_date, tv_cdd, tv_ingresso, tv_editore;
    private int selected = -1;
    private Button btn_confirm;
    private ImageButton btn_calendar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private GregorianCalendar creationDate = null;
    private GregorianCalendar dueDate = null;

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
        tv_cdd = (EditText) findViewById(R.id.et_isbn);
        tv_ingresso = (EditText) findViewById(R.id.et_ingresso);
        tv_editore = (EditText) findViewById(R.id.et_editore);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_calendar=(ImageButton) findViewById(R.id.calendar_button);


        creationDate = (GregorianCalendar) GregorianCalendar.getInstance();
        dueDate = new GregorianCalendar();
        dueDate.setTimeInMillis(creationDate.getTimeInMillis());

        final DatePickerDialog datePicker = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dueDate.set(GregorianCalendar.YEAR, year);
                        dueDate.set(GregorianCalendar.MONTH, monthOfYear);
                        dueDate.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);
                        updateEtDate(); // update the text in the et_date

                    }
                },
                dueDate.get(GregorianCalendar.YEAR),
                dueDate.get(GregorianCalendar.MONTH),
                dueDate.get(GregorianCalendar.DAY_OF_MONTH)
        );

        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });


    }

    private void updateEtDate() {
        ((EditText) findViewById(R.id.et_date)).setText(String.format(String.format("%1$td/%1$tm/%1$tY", dueDate)));
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (pos){
            case (0):
                tv_title.setHint(getString(R.string.hint_title));
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.GONE);
                tv_category.setVisibility(View.GONE);
                tv_cdd.setVisibility(View.GONE);
                tv_name.setVisibility(View.VISIBLE);
                tv_surname.setVisibility(View.VISIBLE);
                tv_date.setVisibility(View.VISIBLE);
                btn_calendar.setVisibility(View.VISIBLE);
                tv_editore.setVisibility(View.GONE);
                tv_ingresso.setVisibility(View.GONE);
                selected = 0;
                break;
            case (1):
                tv_title.setHint(getString(R.string.book_title));
                tv_cdd.setHint("CDD");
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.GONE);
                tv_cdd.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                btn_calendar.setVisibility(View.GONE);
                tv_editore.setVisibility(View.VISIBLE);
                tv_ingresso.setVisibility(View.VISIBLE);
                selected = 1;
                break;
            case (2):
                tv_title.setHint(getString(R.string.album_hint));
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_cdd.setVisibility(View.GONE);
                tv_category.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                btn_calendar.setVisibility(View.GONE);
                tv_editore.setVisibility(View.GONE);
                tv_ingresso.setVisibility(View.GONE);
                selected = 2;
                break;
            case (3):
                tv_title.setHint(getString(R.string.film_hint));
                tv_cdd.setHint("IMDb");
                tv_title.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
                tv_cdd.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.GONE);
                tv_surname.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                btn_calendar.setVisibility(View.GONE);
                tv_editore.setVisibility(View.GONE);
                tv_ingresso.setVisibility(View.GONE);
                selected = 3;
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

        tv_title.setVisibility(View.GONE);
        tv_author.setVisibility(View.GONE);
        tv_category.setVisibility(View.GONE);
        tv_cdd.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_surname.setVisibility(View.GONE);
        tv_date.setVisibility(View.GONE);
        btn_calendar.setVisibility(View.GONE);
        tv_editore.setVisibility(View.GONE);
        tv_ingresso.setVisibility(View.GONE);
        selected = -1;
    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
    private boolean areActivityEditTextNotEmpty(){
        if (tv_title.length()==0){
            tv_title.setError(getString(R.string.insert_title));
            return false;
        } else if (tv_name.length()==0){
            tv_name.setError(getString(R.string.insert_name));
            return false;
        } else if (tv_surname.length()==0) {
            tv_surname.setError(getString(R.string.insert_surname));
            return false;
        } else if (tv_date.length()==0) {
            tv_date.setError(getString(R.string.dateformat_error));
            return false;
        } else {
            return true;
    }}

    private boolean areBookEditTextNotEmpty(){
        String input = entryNumberControl(tv_ingresso.getText().toString());
        if (tv_title.length()==0){
            tv_title.setError(getString(R.string.insert_title));
            return false;
        } else if (tv_author.length()==0){
            tv_author.setError(getString(R.string.insert_author));
            return false;
        } else if(tv_cdd.length()==0) {
            tv_cdd.setError(getString(R.string.insert_cdd));
            return false;
        } else if(tv_editore.length()==0) {
            tv_editore.setError(getString(R.string.insert_edition));
            return false;
        } else if(input==null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean areFilmEditTextNotEmpty(){
        if (tv_title.length()==0){
            tv_title.setError(getString(R.string.insert_title));
            return false;
        } else if (tv_author.length()==0){
            tv_author.setError(getString(R.string.insert_author));
            return false;
        } else if (tv_category.length()==0) {
            tv_category.setError(getString(R.string.insert_category));
            return false;
        }else if(tv_cdd.length()==0) {
            tv_cdd.setError(getString(R.string.insert_imdb));
            return false;
        }else {
            return true;
        }
    }
    private boolean areObjectEditTextNotEmpty(){
        if (tv_title.length()==0){
            tv_title.setError(getString(R.string.insert_title));
            return false;
        } else if (tv_author.length()==0){
            tv_author.setError(getString(R.string.insert_author));
            return false;
        } else if (tv_category.length()==0) {
            tv_category.setError(getString(R.string.insert_category));
            return false;
        }else {
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        //come id univoco prendo il tempo in questo istante in millisecondi
        switch (selected){
            case (0):
                //creare extraActivity
                if (areActivityEditTextNotEmpty()) {
                    // necessario per fare il parsing della data
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    df.setLenient(false);
                    GregorianCalendar date = new GregorianCalendar();
                    try {
                        date.setTime(df.parse(tv_date.getText().toString()));
                    } catch (ParseException e) {
                        tv_date.setError(getString(R.string.dateformat_error));
                    }
                    GregorianCalendar today = new GregorianCalendar();
                    if (today.getTimeInMillis()>=date.getTimeInMillis()){
                        tv_date.setError(getString(R.string.date_in_past));
                    } else {
                        ExtraActivity ea = new ExtraActivity(tv_title.getText().toString(), tv_name.getText().toString(), tv_surname.getText().toString(), date.getTimeInMillis());
                        myRef = database.getReference("activities");
                        myRef.child(String.valueOf(new GregorianCalendar().getTimeInMillis())).setValue(ea);
                        clearTextView();
                        clearErrorTv();
                        Toast.makeText(this, getString(R.string.new_activity), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case(1):
                //creare libro

                if (areBookEditTextNotEmpty()){
                    String id = String.valueOf(new GregorianCalendar().getTimeInMillis());
                    String title = capitalizeFirstLetter(tv_title.getText().toString());
                    String author = capitalizeFirstLetter(tv_author.getText().toString());
                    LibraryObject libraryObject = new LibraryObject(title,author,tv_editore.getText().toString(), Type.BOOK,tv_cdd.getText().toString(), id);
                    libraryObject.setnIngresso(tv_ingresso.getText().toString());

                    myRef = database.getReference("objects");
                    myRef.child(id).setValue(libraryObject);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_book), Toast.LENGTH_LONG).show();
                }


                break;
            case(2):
                //Creare music

                if(areObjectEditTextNotEmpty()){
                    
                    String id = String.valueOf(new GregorianCalendar().getTimeInMillis());
                    String title = capitalizeFirstLetter(tv_title.getText().toString());
                    String author = capitalizeFirstLetter(tv_author.getText().toString());
                    LibraryObject libraryObject = new LibraryObject(title ,author,tv_category.getText().toString(), Type.MUSIC,"0", id);
                    myRef = database.getReference("objects");
                    myRef.child(id).setValue(libraryObject);
                    clearTextView();
                    clearErrorTv();
                    Toast.makeText(this, getString(R.string.new_music), Toast.LENGTH_LONG).show();
                }


                break;
            case(3):
                //creare film
                if(areFilmEditTextNotEmpty()) {

                    String id = String.valueOf(new GregorianCalendar().getTimeInMillis());
                    String title = capitalizeFirstLetter(tv_title.getText().toString());
                    String author = capitalizeFirstLetter(tv_author.getText().toString());
                    LibraryObject libraryObject = new LibraryObject(title, author, tv_category.getText().toString(), Type.FILM,tv_cdd.getText().toString(), id);
                    myRef = database.getReference("objects");
                    myRef.child(id).setValue(libraryObject);
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
        tv_cdd.setText("");
        tv_name.setText("");
        tv_surname.setText("");
        tv_date.setText("");
        tv_editore.setText("");
        tv_ingresso.setText("");
    }

    private void clearErrorTv(){
        tv_title.setError(null);
        tv_author.setError(null);
        tv_category.setError(null);
        tv_cdd.setError(null);
        tv_name.setError(null);
        tv_surname.setError(null);
        tv_date.setError(null);
        tv_editore.setError(null);
        tv_ingresso.setError(null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        creationDate = (GregorianCalendar) GregorianCalendar.getInstance();
        dueDate = new GregorianCalendar();
        dueDate.setTimeInMillis(creationDate.getTimeInMillis());

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
    private String capitalizeFirstLetter(String input){
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }
}
