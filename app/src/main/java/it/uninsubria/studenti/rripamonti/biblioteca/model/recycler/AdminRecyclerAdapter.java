package it.uninsubria.studenti.rripamonti.biblioteca.model.recycler;

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
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

/**
 * Created by rober on 19/04/2017.
 */

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.LibraryItemHolder> {
    private ArrayList<LibraryObject> items;
    public AdminRecyclerAdapter(ArrayList<LibraryObject> mItem) {
        items = mItem;
    }

    @Override
    public AdminRecyclerAdapter.LibraryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new AdminRecyclerAdapter.LibraryItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(LibraryItemHolder holder, int position) {
        LibraryObject item = items.get(position);
        holder.bindLibraryItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class LibraryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView itemImage;
        private TextView tvUser;
        private TextView tvTitle;
        private LibraryObject mLibraryItem = new LibraryObject();

        public LibraryItemHolder(View v){
            super(v);
            itemImage = (ImageView) v.findViewById(R.id.item_image);
            tvTitle = (TextView) v.findViewById(R.id.item_title);
            tvUser = (TextView) v.findViewById(R.id.item_author);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView","CLICK!");
            //lancia schermata accetta rifiuta prestito

        }

        public void bindLibraryItem(LibraryObject item){

            mLibraryItem = item;
            itemImage.setImageResource(R.mipmap.ic_launcher);

            tvTitle.setText(mLibraryItem.getTitle());}
    }
}

