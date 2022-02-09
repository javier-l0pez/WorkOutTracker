package com.jls.workouttracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jls.workouttracker.R;
import com.jls.workouttracker.model.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText textUsername, textEmail, textPass, textConPass;
    private Button btnSave, btnLogin;
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
        btnLogin = findViewById(R.id.btn_goto_log);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnSave.setOnClickListener(v -> {
            fdb = FirebaseDatabase.getInstance();
            myRef = fdb.getReference().child("user");
            fAuth = FirebaseAuth.getInstance();

            if (textPass.length() < 6) {
                Snackbar.make(v, R.string.pass_leng, Snackbar.LENGTH_LONG).show();
            } else if (textUsername.getText().toString().isEmpty()) {
                Snackbar.make(v, R.string.usrn_emp, Snackbar.LENGTH_LONG).show();
            } else if (textPass.equals(textConPass.getText().toString())) {
                Snackbar.make(v, R.string.pass_match, Snackbar.LENGTH_LONG).show();
            } else if ((!textEmail.getText().toString().isEmpty()) && (!textPass.getText().toString().isEmpty()) && (textPass.length() >= 6) && !textUsername.getText().toString().isEmpty()) {
                fAuth.createUserWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                User newUser = new User();
                                newUser.setId(fAuth.getUid());

                                newUser.setUsername(textUsername.getText().toString());
                                newUser.setEmail(textEmail.getText().toString());
                                newUser.setPassword(textPass.getText().toString());
                                newUser.setAdmin(false);

                                myRef.child(newUser.getId()).setValue(newUser);
                                Toast.makeText(RegisterActivity.this, R.string.sgnup_succ, Toast.LENGTH_LONG).show();

                                fAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString()).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, WorkoutsActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(RegisterActivity.this, R.string.login_fai, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, R.string.sgnup_fl, Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Snackbar.make(v, R.string.mailpassfill, Snackbar.LENGTH_LONG).show();
            }
        });

    }
}