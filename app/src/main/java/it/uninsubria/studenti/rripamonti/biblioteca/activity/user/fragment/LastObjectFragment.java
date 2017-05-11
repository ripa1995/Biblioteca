package it.uninsubria.studenti.rripamonti.biblioteca.activity.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;


public class LastObjectFragment extends Fragment {
    private static final String TAG = "LastObjectFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("objects");


    public static class LibraryObjectHolder extends RecyclerView.ViewHolder {
        ImageView mItemImage;
        TextView mItemAuthor;
        TextView mItemTitle;



        public LibraryObjectHolder(View v){
            super(v);
            mItemImage = (ImageView) v.findViewById(R.id.item_image);
            mItemAuthor = (TextView) v.findViewById(R.id.item_author);
            mItemTitle = (TextView) v.findViewById(R.id.item_title);

        }
    }

    public LastObjectFragment() {
        // Required empty public constructor
    }

    public static LastObjectFragment newInstance() {
        LastObjectFragment fragment = new LastObjectFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_last_object, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.objectsRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        ref.keepSynced(true);
        Query myRef = ref.limitToLast(25);
        adapter = new FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder>(LibraryObject.class, R.layout.recyclerview_item_row,LibraryObjectHolder.class, myRef) {
            @Override
            protected void populateViewHolder(LibraryObjectHolder viewHolder, LibraryObject model, int position) {
                switch (model.getType().toString()){
                    case "BOOK":
                        //immagine libro
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_book);
                        break;
                    case "FILM":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_movie);
                        break;
                    case "MUSIC":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_music_1);
                        break;
                }

                viewHolder.mItemTitle.setText(model.getTitle());
                viewHolder.mItemAuthor.setText(model.getAuthor());
            }
        };
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

            //da fare come launcher test ma con child invece che value!

}
