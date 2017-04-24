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
import it.uninsubria.studenti.rripamonti.biblioteca.model.ExtraActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.Item;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

/**
 * Created by rober on 14/04/2017.
 */
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.LibraryItemHolder> {
    private ArrayList<Object> items;
    public UserRecyclerAdapter(ArrayList<Object> mItem) {
        items = mItem;
    }

    @Override
    public UserRecyclerAdapter.LibraryItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new LibraryItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(UserRecyclerAdapter.LibraryItemHolder holder, int position) {
        Object item = items.get(position);
        holder.bindLibraryItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class LibraryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImage;
        private TextView mItemAuthor, mItemType;
        private TextView mItemTitle;
        private LibraryObject mLibraryItem;
        private ExtraActivity mExtraItem;

        public LibraryItemHolder(View v){
            super(v);
            mItemImage = (ImageView) v.findViewById(R.id.item_image);
            mItemAuthor = (TextView) v.findViewById(R.id.item_author);
            mItemTitle = (TextView) v.findViewById(R.id.item_title);
            mItemType = (TextView) v.findViewById(R.id.item_type);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView","CLICK!");
        }

        public void bindLibraryItem(Object item){
            if (item instanceof LibraryObject){
                mLibraryItem = (LibraryObject) item;
                mItemImage.setImageResource(R.mipmap.ic_launcher);
                mItemTitle.setText(mLibraryItem.getTitle());
                mItemAuthor.setText(mLibraryItem.getAuthor());
                mItemType.setText(mLibraryItem.getType().toString());
            } else if (item instanceof ExtraActivity){
                mExtraItem = (ExtraActivity) item;
                mItemImage.setImageResource(R.mipmap.ic_launcher);

                mItemTitle.setText(mExtraItem.getTitle());
            }
        }
    }
}
