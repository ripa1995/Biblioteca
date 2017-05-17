package it.uninsubria.studenti.rripamonti.biblioteca.model.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;

/**
 * Created by rober on 17/05/2017.
 */

public class LoanStatusHolder extends RecyclerView.ViewHolder {

    public ImageView itemImage;
    public TextView tvStartOfLoan;
    public TextView tvEndOfLoan;
    public TextView tvTitle;
    public View mView;
    public LibraryObject mLibraryItem = new LibraryObject();

    public LoanStatusHolder(View v) {
        super(v);
        mView = v;
        itemImage = (ImageView) v.findViewById(R.id.item_image);
        tvTitle = (TextView) v.findViewById(R.id.item_title);
        tvStartOfLoan = (TextView) v.findViewById(R.id.item_dateOfLoan);
        tvEndOfLoan = (TextView) v.findViewById(R.id.item_endOfLoan);

    }
}