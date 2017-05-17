package it.uninsubria.studenti.rripamonti.biblioteca.activity.user.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import it.uninsubria.studenti.rripamonti.biblioteca.R;
import it.uninsubria.studenti.rripamonti.biblioteca.model.ExtraActivity;
import it.uninsubria.studenti.rripamonti.biblioteca.model.holder.ActivitiesHolder;


public class LastExtraActivitiesFragment extends Fragment {


    private static final String TAG = "LastObjectFragment";
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<ExtraActivity, ActivitiesHolder> adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("activities");

    private GregorianCalendar gc = new GregorianCalendar();
    private ExtraActivity ea;




    public LastExtraActivitiesFragment() {
        // Required empty public constructor
    }

    public static LastExtraActivitiesFragment newInstance() {
        LastExtraActivitiesFragment fragment = new LastExtraActivitiesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_last_extra_activities, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.activitiesRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ref.keepSynced(true);

        Query myRef = ref.orderByChild("date").startAt(new GregorianCalendar().getTimeInMillis());
        adapter = new FirebaseRecyclerAdapter<ExtraActivity, ActivitiesHolder>(ExtraActivity.class, R.layout.recyclerview_item_row,ActivitiesHolder.class, myRef) {
            @Override
            protected void populateViewHolder(ActivitiesHolder viewHolder, ExtraActivity model, int position) {
                viewHolder.mItemImage.setImageResource(R.drawable.ic_action_calendar_day);
                String mdate = new SimpleDateFormat("dd/MM/yyyy").format(model.getDate());
                viewHolder.mItemAuthor.setText(mdate);
                viewHolder.mItemTitle.setText(model.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
