package it.uninsubria.studenti.rripamonti.biblioteca.activity.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.activity.user.ObjectDetail;
import it.uninsubria.studenti.rripamonti.biblioteca.model.LibraryObject;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.LibraryObjectHolder;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Album;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.AlbumService;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.Movie;
import it.uninsubria.studenti.rripamonti.biblioteca.rest.MovieService;
/*
mostra gli ultimi oggetti aggiunti al catalogo
 */

public class LastObjectFragment extends Fragment {
    private static final String TAG = "LastObjectFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private static ArrayList<LibraryObject> items = new ArrayList<LibraryObject>();
    FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("objects");

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
        final View rootView = inflater.inflate(R.layout.fragment_last_object, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.objectsRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        ref.keepSynced(true);
        Query myRef = ref.limitToLast(25);
        adapter = new FirebaseRecyclerAdapter<LibraryObject, LibraryObjectHolder>(LibraryObject.class, R.layout.recyclerview_item_row,LibraryObjectHolder.class, myRef) {
            @Override
            protected void populateViewHolder(final LibraryObjectHolder viewHolder, LibraryObject model, final int position) {
                switch (model.getType().toString()){
                    case "BOOK":
                        //immagine libro
                        Picasso.with(rootView.getContext()).load("http://covers.openlibrary.org/b/isbn/"+model.getIsbn()+"-M.jpg?default=false").placeholder(R.drawable.ic_action_book).error(R.drawable.ic_action_book).into(viewHolder.mItemImage);
                        break;
                    case "FILM":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_movie);
                        MovieService.getInstance(rootView.getContext()).getMovie(model.getIsbn(), new MovieService.Callback() {
                            @Override
                            public void onLoad(Movie movie) {
                                if (movie != null) {
                                    Picasso.with(rootView.getContext()).load(movie.getPosterUrl()).placeholder(R.drawable.ic_action_movie).error(R.drawable.ic_action_movie).into(viewHolder.mItemImage);
                                }
                            }
                            @Override
                            public void onFailure() {

                            }
                        });
                        break;
                    case "MUSIC":
                        viewHolder.mItemImage.setImageResource(R.drawable.ic_action_music_1);
                        AlbumService.getInstance(rootView.getContext()).getAlbum(model.getAuthor(), model.getTitle(), new AlbumService.Callback() {
                            @Override
                            public void onLoad(Album album) {
                                if (album!=null) {
                                    List<Album.Image> list = album.getImages();
                                    Log.d(TAG, album.getArtist());
                                    for (Album.Image image : list) {
                                        if (image.getSize().equals("medium")) {
                                            Picasso.with(rootView.getContext()).load(image.getText()).placeholder(R.drawable.ic_action_music_1).error(R.drawable.ic_action_music_1).into(viewHolder.mItemImage);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
                        break;
                }
                items.add(position, model);
                viewHolder.mItemTitle.setText(model.getTitle());
                viewHolder.mItemAuthor.setText(model.getAuthor());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LibraryObject lo = items.get(position);

                        Intent intent = new Intent(v.getContext(),ObjectDetail.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("key",lo);
                        intent.putExtras(extras);
                        v.getContext().startActivity(intent);
                    }
                });
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
