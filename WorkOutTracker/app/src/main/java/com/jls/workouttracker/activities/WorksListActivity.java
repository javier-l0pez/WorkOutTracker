package com.jls.workouttracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jls.workouttracker.NewDialog;
import com.jls.workouttracker.R;
import com.jls.workouttracker.adapters.ExerciseAdapter;
import com.jls.workouttracker.model.Exercise;
import com.jls.workouttracker.model.User;
import java.util.ArrayList;

public class WorksListActivity extends AppCompatActivity implements NewDialog.newDialogListener {

    private RecyclerView rview;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    private FirebaseAuth fAuth;
    private DatabaseReference myRef;

    private FloatingActionButton btnAddEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_list);

        fAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("user");
        myRef.child(fAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User myUser = snapshot.getValue(User.class);
                if (! myUser.isAdmin()) {
                    iAmNotAdmin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rview = findViewById(R.id.rview);
        rview.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);

        rview.setLayoutManager(myLayoutManager);
        ArrayList<Exercise> exList = new ArrayList<>();
        myAdapter = new ExerciseAdapter(this, exList);
        rview.setAdapter(myAdapter);

        myRef = FirebaseDatabase.getInstance().getReference("pworkouts");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Exercise exercise = dataSnapshot.getValue(Exercise.class);
                    exList.add(exercise);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAddEx = findViewById(R.id.btn_add_exer);
        btnAddEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        NewDialog newDialog = new NewDialog();
        newDialog.show(getSupportFragmentManager(), "New Exercise Dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_default, menu);
        MenuItem item = menu.findItem(R.id.op_pwork);
        item.setVisible(false);
        item = menu.findItem(R.id.op_custom_w);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.op_signout:
                fAuth.signOut();
                Toast.makeText(WorksListActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.op_custom_w:
                Intent intent = new Intent(WorksListActivity.this, CustomsActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        fAuth.signOut();
        Toast.makeText(WorksListActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void applyTexts(String name, String muscle) {
        Exercise exercise = new Exercise();
        exercise.setName(name);
        exercise.setMuscle(muscle);
        myRef.push().setValue(exercise);
    }

    public void iAmNotAdmin(){
        View b = findViewById(R.id.btn_add_exer);
        b.setVisibility(View.GONE);
    }
}