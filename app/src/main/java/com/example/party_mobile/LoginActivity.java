package com.example.party_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
public class LoginActivity extends BaseActivity {

    private EditText txtEmail, txtPassword;
    private TextView txtForgetPassword;
    private Button btnSignUp, logIn;
    String password, email;

    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private String userType;
    DocumentReference document;

    private Spinner til_user_type;
    private String store_user_type;
    Integer number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        txtEmail=findViewById(R.id.edtEmail);
        txtPassword=findViewById(R.id.edtPassword);
        txtForgetPassword=findViewById(R.id.txtForgetPassword);
        btnSignUp=findViewById(R.id.btnSignUp);
        logIn=findViewById(R.id.btnLogin);
        til_user_type = findViewById(R.id.til_user_type);

        selectUserTypeSpinner();


//        btnSignUp.setOnClickListener(view -> {
//            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//        });

//        txtForgetPassword.setOnClickListener(view -> {
//            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//        });

        logIn.setOnClickListener(view -> {
            if (areFieldReady()) {
                if (store_user_type.equals("--Select user type--")) {
                    Toast.makeText(this, "select user type", Toast.LENGTH_SHORT).show();
                }else{
                    login();
                }

            }
        });

    }

    private void login() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {

                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> email) {
                                if (email.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Please verify email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error : " + email.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean areFieldReady() {

        email = txtEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        boolean flag = false;
        View requestView = null;

        if (email.isEmpty()) {
            txtEmail.setError("Field is required");
            flag = true;
            requestView = txtEmail;
        } else if (password.isEmpty()) {
            txtPassword.setError("Field is required");
            flag = true;
            requestView = txtPassword;
        } else if (password.length() < 8) {
            txtPassword.setError("Minimum 8 characters");
            flag = true;
            requestView = txtPassword;

        }

        if (flag) {
            requestView.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public void selectUserTypeSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        til_user_type.setAdapter(adapter);

        til_user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                LoginActivity.this.number = til_user_type
                        .getSelectedItemPosition() + 1;

                store_user_type = til_user_type.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

    }



}

