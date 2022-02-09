package com.jls.workouttracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jls.workouttracker.R;
import com.jls.workouttracker.fragments.PersonalWorkoutFragment;
import com.jls.workouttracker.fragments.PublicWorkoutFragment;

public class WorkoutsActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference myRef;
    private Bundle bundle;
    public boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        fAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("user").child(fAuth.getUid()).child("admin");
        readData(myRef, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                isAdmin = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                bundle = new Bundle();
                bundle.putBoolean("isAdmin", isAdmin);

                BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                bottomNav.setOnItemSelectedListener(item -> {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_public:
                            selectedFragment = new PublicWorkoutFragment();
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.nav_personal:
                            selectedFragment = new PersonalWorkoutFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                });

                Fragment fragment_initial = new PublicWorkoutFragment();
                fragment_initial.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_initial).commit();
            }

            @Override
            public void onStart() {
                //when starting
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        fAuth.signOut();
        Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
        finish();
    }

    public interface OnGetDataListener {
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onSuccess(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });
    }

}