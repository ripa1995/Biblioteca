package it.uninsubria.studenti.rripamonti.biblioteca.activity.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

public class ObjectDetail extends AppCompatActivity {
    private LibraryObject lo;
    private static final String TAG = "Object Detail";
    private ImageView mItemImage;
    private TextView mItemTitle, mItemAuthor, mItemISBN, mItemCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initUi();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            lo = (LibraryObject) extras.getSerializable("key");
        }
         populateView();

    }
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG,"onSupportNavigateUp()");
        finish();
        return true;
    }
    private void initUi(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         mItemImage = (ImageView) findViewById(R.id.item_image);
         mItemTitle = (TextView) findViewById(R.id.tv_title);
         mItemAuthor = (TextView) findViewById(R.id.tv_author);
         mItemCategory = (TextView) findViewById(R.id.tv_category);
         mItemISBN = (TextView) findViewById(R.id.tv_isbn);
    }
    private void populateView(){
        switch (lo.getType().toString()) {
            case "BOOK":
                //immagine libro
                mItemImage.setImageResource(R.drawable.ic_action_book);
                mItemISBN.setText(lo.getIsbn());
                mItemISBN.setVisibility(View.VISIBLE);
                break;
            case "FILM":
                mItemImage.setImageResource(R.drawable.ic_action_movie);
                mItemISBN.setVisibility(View.GONE);
                break;
            case "MUSIC":
                mItemImage.setImageResource(R.drawable.ic_action_music_1);
                mItemISBN.setVisibility(View.GONE);
                break;
        }

        mItemTitle.setText(lo.getTitle());
        mItemAuthor.setText(lo.getAuthor());
        mItemCategory.setText(lo.getCategory());
    }
}
