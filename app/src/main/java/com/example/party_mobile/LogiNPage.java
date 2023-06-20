package com.example.party_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogiNPage extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail,loginPassword;
    private TextView forgotRedirectText;
    private Button LoginButton, sign_up;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        //Hide Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState); setContentView(R.layout.loginpage);

        auth= FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        LoginButton = findViewById(R.id.login_button);
        forgotRedirectText=findViewById(R.id.forgotRedirectText);
        sign_up=findViewById(R.id.sign_up_button);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                if(!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LogiNPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LogiNPage.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogiNPage.this,"Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        loginPassword.setError("Password cannot be empty");
                    }
                }else if (email.isEmpty()){
                        loginEmail.setError("Email cannot be empty");
                    } else {
                        loginEmail.setError("Please enter valid email");
                    }
                }


        });

        forgotRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogiNPage.this,forgot_password.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogiNPage.this,SignUpActivity.class));
            }
        });
}
}
