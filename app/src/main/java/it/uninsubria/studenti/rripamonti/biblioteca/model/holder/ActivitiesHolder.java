package it.uninsubria.studenti.rripamonti.biblioteca.model.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.uninsubria.studenti.rripamonti.biblioteca.R;

/**
 * Created by rober on 17/05/2017.
 */

public class ActivitiesHolder extends RecyclerView.ViewHolder {
    public ImageView mItemImage;
    public TextView mItemAuthor;
    public TextView mItemTitle;



    public ActivitiesHolder(View v){
        super(v);
        mItemImage = (ImageView) v.findViewById(R.id.item_image);
        mItemAuthor = (TextView) v.findViewById(R.id.item_author);
        mItemTitle = (TextView) v.findViewById(R.id.item_title);
    }
}