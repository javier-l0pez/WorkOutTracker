package com.jls.workouttracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.jls.workouttracker.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnReg;
    private TextInputEditText textEmail;
    private TextInputEditText textPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLogin = findViewById(R.id.btn_main_log);
        btnReg = findViewById(R.id.btn_main_reg);
        textEmail = findViewById(R.id.email_txt);
        textPass = findViewById(R.id.pass_txt);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> {
            if ((!textEmail.getText().toString().isEmpty()) && (!textPass.getText().toString().isEmpty())) {
                fAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPass.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.login_suc, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, WorkoutsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.login_fai, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnReg.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });


    }


}