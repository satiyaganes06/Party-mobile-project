package com.example.party_mobile;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.party_mobile.Firebase_Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class user_profile extends AppCompatActivity {

    private UserModel userModel;

    private EditText firstNameEditText, lastNameEditText, studentIdEditText, emailEditText, phoneNumberEditText;
    private Button saveProfileButton;
    private ImageView profileImageView;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

//    public user_profile(String firstName, String lastName, String studentId, String email, String phoneNumber) {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Profile");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);
        profileImageView = findViewById(R.id.profileImageView);

        // Load the user profile from Firestore
        loadUserProfile();

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void loadUserProfile() {
        // Retrieve the user profile from Firestore
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //user_profile userProfile = document.toObject(user_profile.class);

                        // Set the retrieved values to the corresponding views
                        firstNameEditText.setText(document.getString(""));
//                        lastNameEditText.setText(userProfile.getLastName());
//                        studentIdEditText.setText(userProfile.getStudentId());
//                        emailEditText.setText(userProfile.getEmail());
//                        phoneNumberEditText.setText(userProfile.getPhoneNumber());
                    }
                } else {
                    Toast.makeText(user_profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getPhoneNumber() {
        return 0;
    }

    private char getEmail() {
        return 0;
    }

    private char getStudentId() {
        return 0;
    }

    private char getLastName() {
        return 0;
    }

    private char getFirstName() {
        return 0;
    }

    private void saveUserProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String studentId = studentIdEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

     //   user_profile userProfile = new user_profile(firstName, lastName, studentId, email, phoneNumber);

        // Save the user profile to Firestore
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.set(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(user_profile.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(user_profile.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //et_address.setText(document.getString("party_address"));


    }
}
