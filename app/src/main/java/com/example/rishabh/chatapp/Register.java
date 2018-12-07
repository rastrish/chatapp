package com.example.rishabh.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {
    Button signupButton, loginButton;
    EditText emailText, passwordText, confirmText;
    FirebaseAuth mAuth;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signupButton = findViewById(R.id.SIGNUP);
        loginButton = findViewById(R.id.RG_LGN);
        progressbar = findViewById(R.id.progressBar);


        emailText = findViewById(R.id.RG_EMAIL_TEXT);
        confirmText = findViewById(R.id.CONFRM_PASS);
        passwordText = findViewById(R.id.RG_PASS_TEXT);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();

            }
        });


    }

    private void createUser() {
        String email = emailText.getText().toString().trim();
        String pass = passwordText.getText().toString().trim();
        String confrmPass = confirmText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Field Can't be left empty", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Field Can't be left empty", Toast.LENGTH_SHORT).show();

        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Password should contain min 6 char", Toast.LENGTH_SHORT).show();
        }

        if (!pass.equals(confrmPass)) {
            Toast.makeText(this, "Password and confirm  password does not match", Toast.LENGTH_SHORT).show();

        } else {

            progressbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent setupIntent = new Intent(Register.this,account.class);
                        startActivity(setupIntent);
                        finish();
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(Register.this, "Email Address is Already Register", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, ""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressbar.setVisibility(View.GONE);
                }
            });


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendtomain();

        }
    }

    private void sendtomain() {
        Intent mainintent = new Intent(Register.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }
}