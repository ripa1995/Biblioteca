package it.uninsubria.studenti.rripamonti.biblioteca.model.recycler;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.RequestLoanActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

/**
 * Created by rober on 17/04/2017.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.LibraryItemHolder> {
    private ArrayList<LibraryObject> items;
    public SearchRecyclerAdapter(ArrayList<LibraryObject> mItem) {
        items = mItem;
    }

    @Override
    public SearchRecyclerAdapter.LibraryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row_semaphore, parent, false);
        return new LibraryItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(SearchRecyclerAdapter.LibraryItemHolder holder, int position) {
        LibraryObject item = items.get(position);
        holder.bindLibraryItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class LibraryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemDescription;
        private LibraryObject mLibraryItem = new LibraryObject();

        public LibraryItemHolder(View v){
            super(v);
            mItemImage = (ImageView) v.findViewById(R.id.item_image2);
            mItemDate = (TextView) v.findViewById(R.id.item_date2);
            mItemDescription = (TextView) v.findViewById(R.id.item_description2);
            mItemImage = (ImageView) v.findViewById(R.id.item_image3);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView","CLICK!");
            //se verde mando a richiesta prenotazione
            //se giallo mando a status loan
            //se rosso toast con non disponibile
            Intent intent = new Intent(v.getContext(), RequestLoanActivity.class);
            ContextCompat.startActivity(v.getContext(), intent, null);
        }

        public void bindLibraryItem(LibraryObject item){
            mLibraryItem = item;
            mItemImage.setImageResource(R.mipmap.ic_launcher);

            mItemDescription.setText(mLibraryItem.getTitle());
            //se disponibile semaforo verde, in prenotazione giallo, non disponibile rosso

        }
    }
}
