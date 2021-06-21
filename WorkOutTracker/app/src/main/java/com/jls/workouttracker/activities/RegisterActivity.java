package com.jls.workouttracker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jls.workouttracker.R;
import com.jls.workouttracker.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText textUsername, textEmail, textPass, textConPass;
    private Button btnSave;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fdb;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textUsername = findViewById(R.id.reg_username);
        textEmail = findViewById(R.id.reg_email);
        textPass = findViewById(R.id.reg_pass);
        textConPass = findViewById(R.id.reg_con_pass);
        btnSave = findViewById(R.id.btn_cnf_reg);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fdb = FirebaseDatabase.getInstance();
                myRef = fdb.getReference().child("user");
                fAuth = FirebaseAuth.getInstance();

                if ((! textEmail.getText().toString().isEmpty()) && (! textPass.getText().toString().isEmpty()) && (textPass.length() >= 6)){
                    fAuth.createUserWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    User newUser = new User();
                                    newUser.setId(fAuth.getUid());

                                    newUser.setUsername(textUsername.getText().toString());
                                    newUser.setEmail(textEmail.getText().toString());
                                    newUser.setPassword(textPass.getText().toString());
                                    newUser.setAdmin(false);

                                    myRef.child(newUser.getId()).setValue(newUser);
                                    Toast.makeText(RegisterActivity.this, "Signup successful", Toast.LENGTH_LONG).show();

                                    fAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, WorksListActivity.class);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    finish();
                                    return;
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Signup failed", Toast.LENGTH_LONG).show();
                                }
                            });

                } else if ((! textEmail.getText().toString().isEmpty()) && (! textPass.getText().toString().isEmpty()) && (textPass.length() < 6)){
                    Snackbar.make(v, "Password must be at least 6 characters length.", Snackbar.LENGTH_LONG).show();
                }  else {
                    Snackbar.make(v, "Email and password must be filled.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}