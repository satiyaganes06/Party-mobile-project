package com.example.party_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;

import java.util.Objects;

public class forgot_password extends AppCompatActivity {
    private EditText emailEditText;
    public TextView backLogin;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setTitle("Forgot Password");

        emailEditText = findViewById(R.id.emailEditText);
        Button resetButton = findViewById(R.id.resetButton);
        backLogin = findViewById(R.id.BackLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(forgot_password.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                resetPassword(email);
            }
        });
        backLogin.setOnClickListener(view -> finish());
    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(forgot_password.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidUserException invalidEmailException) {
                            Toast.makeText(forgot_password.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthRecentLoginRequiredException recentLoginException) {
                            Toast.makeText(forgot_password.this, "User recently logged in, please reauthenticate", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(forgot_password.this, "Password reset failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}