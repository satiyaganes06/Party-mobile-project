package com.example.party_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends BaseActivity {
    private FirebaseAuth firebaseAuth;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener((v) ->
        {
            if(firebaseAuth.getCurrentUser() != null) {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();

        });
    }

}