package com.example.rishabh.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    Button registerButton, loginButton;
    ProgressBar progressBar;
    EditText emailText, passwordText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.REG_BUT);
        loginButton = findViewById(R.id.LOGIN_BUT);
        passwordText = findViewById(R.id.RG_PASS_TEXT);
        emailText = findViewById(R.id.RG_EMAIL_TEXT);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerintent = new Intent(Login.this, Register.class);
                startActivity(registerintent);
                finish();
            }
        });
    }

    private void loginUser() {
        String email = emailText.getText().toString().trim();
        String pass = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "field cant be left empty", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "field cant be left empty", Toast.LENGTH_SHORT).show();

        } else {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        sendtomain();
                    }
                    else
                    {
                        Toast.makeText(Login.this, ""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null )
        {
            sendtomain();

        }
    }

    private void sendtomain()
    {
       Intent mainintent = new Intent(Login.this,MainActivity.class);
        startActivity(mainintent);
        finish();
    }
}


