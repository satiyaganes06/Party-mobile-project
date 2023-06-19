package com.example.party_mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.party_mobile.Firebase_Model.PartyDetailsModel;
import com.example.party_mobile.Firebase_Model.UserModel;
import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.UploadTask;

import java.security.SecureRandom;

public class SignUpActivity extends BaseActivity {

    private FirebaseAuth auth;

    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private EditText name,signupEmail, phoneNum, age, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;

    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //Hide Action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        signupEmail = findViewById(R.id.signup_email);
        phoneNum = findViewById(R.id.phoneNum);
        age = findViewById(R.id.age);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String user_name = name.getText().toString().trim();
                String phone_Num = phoneNum.getText().toString().trim();
                String user_age = age.getText().toString().trim();

                if (user_email.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }else if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else if (user_name.isEmpty()){
                    name.setError("Name cannot be empty");
                }else if (phone_Num.isEmpty()){
                    phoneNum.setError("Phone Number cannot be empty");
                }else if (user_age.isEmpty()){
                    age.setError("Age cannot be empty");
                }else {
                    auth.createUserWithEmailAndPassword(user_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser userID = auth.getCurrentUser();
                                signUp(userID.getUid(), user_email, user_name, Integer.parseInt(phone_Num), Integer.parseInt(user_age));

                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

       loginRedirectText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });


    }

    private void signUp(String user_id,String email, String name, int phone_num, int user_age){
        loadingDialog.startLoadingDialog();

        UserModel userDetails;
        userDetails = new UserModel(email, name,"", user_id, phone_num, user_age);

        mFireStore.collection("users")
                .document(user_id)
                .set(userDetails, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingDialog.dismisDialog();
                        Toast.makeText(getApplicationContext(), "Sign Up successfully !!!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), LogiNPage.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismisDialog();
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static String generateRandomID() {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < 20; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

}
