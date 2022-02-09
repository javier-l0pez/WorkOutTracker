package com.jls.workouttracker.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jls.workouttracker.DAOexercise;
import com.jls.workouttracker.R;
import com.jls.workouttracker.activities.AddEditActivity;
import com.jls.workouttracker.adapters.RVAdapter;
import com.jls.workouttracker.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class PublicWorkoutFragment extends Fragment {

    private RecyclerView rview;
    private RVAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private View view;

    private List<Exercise> exs;
    private DAOexercise dao;
    public static final String EXTRA_PUBLIC = "com.jls.workouttracker.EXTRA_PUBLIC";
    private boolean amIAdmin = false;
    private FloatingActionButton btnAddEx;


    public PublicWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exs = new ArrayList<>();
        dao = new DAOexercise(true);
        loadData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_workout, container, false);
        rview = view.findViewById(R.id.fr_pub_rview);
        btnAddEx = view.findViewById(R.id.btn_add_exer);
        if (getArguments() != null) {
            amIAdmin = getArguments().getBoolean("isAdmin");
            if (amIAdmin) {
                btnAddEx.setVisibility(view.VISIBLE);
                btnAddEx.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), AddEditActivity.class);
                    intent.putExtra(EXTRA_PUBLIC, true);
                    startActivity(intent);
                });
            }
        }


        initRecycler();
        return view;
    }

    private void initRecycler() {

        rview.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getActivity());
        rview.setLayoutManager(myLayoutManager);
        myAdapter = new RVAdapter(getActivity(), true, amIAdmin);
        rview.setAdapter(myAdapter);

    }

    private void loadData() {
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Exercise ex = dataSnapshot.getValue(Exercise.class);
                    ex.setKey(dataSnapshot.getKey());
                    exs.add(ex);
                }
                myAdapter.setItems(exs);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}