package com.example.party_mobile;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.party_mobile.Firebase_Model.PartyDetailsModel;
import com.example.party_mobile.Firebase_Model.UserModel;
import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


public class user_profile extends BaseActivity {

    private UserModel userModel;

    private EditText nameEditText, emailEditText, phoneNumberEditText, ageEditText;
    private Button saveProfileButton, logOutButton;
    private ImageView profileImageView;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    DocumentSnapshot document;

    LoadingDialog loadingDialog = new LoadingDialog(this);

    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Profile");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        logOutButton = findViewById(R.id.logOutButton);
        profileImageView = findViewById(R.id.profileImageView);

        // Load the user profile from Firestore
        loadUserProfile();

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    public void loadUserProfile(){
        loadingDialog.startLoadingDialog();
        mFireStore.collection("users")
                .document(getCurrentUserID())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            document = task.getResult();
                            if (document.exists()) {
                                setUserDetails();
                            } else {
                                Toast.makeText(user_profile.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(user_profile.this, "Fail to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setUserDetails() {

        Glide.with(this)
                .load(document.getString("image"))
                .into(profileImageView);

        nameEditText.setText(document.getString("username"));
        ageEditText.setText(String.valueOf(document.get("age")));
        emailEditText.setText(document.getString("email"));
        phoneNumberEditText.setText(String.valueOf(document.get("phoneNum")));

        loadingDialog.dismisDialog();
    }

    private void saveUserProfile() {
        String id= document.getString("userID");
        String username = nameEditText.getText().toString();
        String user_email = emailEditText.getText().toString();
        int age = Integer.parseInt(ageEditText.getText().toString());
        int phoneNumber = Integer.parseInt(phoneNumberEditText.getText().toString());

        UserModel userDetails = new UserModel(user_email, username,"https://cdn-icons-png.flaticon.com/512/3607/3607444.png", id, phoneNumber, age);

        mFireStore.collection("users")
                .document(getCurrentUserID())
                .set(userDetails, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog.dismisDialog();
                        Toast.makeText(user_profile.this, "User details updated successfully", Toast.LENGTH_SHORT).show();

                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismisDialog();
                        Toast.makeText(user_profile.this, "Failed to update user details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
