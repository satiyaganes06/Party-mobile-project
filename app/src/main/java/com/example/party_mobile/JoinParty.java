package com.example.party_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class JoinParty extends BaseActivity {
    LoadingDialog loadingDialog = new LoadingDialog(this);

    private String documentID;
    DocumentSnapshot document;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Uri imageUri;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private TextView tv_party_category,tv_type, tv_date, tv_time, tv_item_name, tv_address, tv_city_status, tv_telNum, tv_current_people, tv_total_people;
    private ImageView iv_qr_code, iv_party_image;
    private Button btn_Join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_party);

        documentID = getIntent().getStringExtra("party_id");

        fireStoreForm();
//
//        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        setTitle("Join Party");
// showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_Join = findViewById(R.id.btn_Join);
//        btn_Join.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mFireStore.collection("partyRoom")
//                        .document(documentID)
//                        .set(new HashMap<String, Object>() {
//                            {
//                                put("party_id", documentID);
//                                put("party_join_code", document.getString("party_join_code"));
//                                put("user_id_list", Arrays.asList(getCurrentUserID()));
//                            }
//                        })
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                loadingDialog.dismisDialog();
//                                Toast.makeText(JoinParty.this, "Joined Party Room Successfully !!!", Toast.LENGTH_LONG).show();
//                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(i);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                loadingDialog.dismisDialog();
//                                Toast.makeText(JoinParty.this, "Server Error", Toast.LENGTH_LONG).show();
//                            }
//                        });
//            }
//        });
//        btn_Join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DocumentReference partyRoomRef = mFireStore.collection("partyRoom").document(documentID);
//                partyRoomRef.update("user_id_list", FieldValue.arrayUnion(getCurrentUserID()))
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                loadingDialog.dismisDialog();
//                                Toast.makeText(JoinParty.this, "Joined Party Room Successfully!", Toast.LENGTH_LONG).show();
//                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(i);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                loadingDialog.dismisDialog();
//                                Toast.makeText(JoinParty.this, "Server Error", Toast.LENGTH_LONG).show();
//                            }
//                        });
//            }
//        });
        btn_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference partyRoomRef = mFireStore.collection("partyRoom").document(documentID);
                partyRoomRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Object userIDs = document.get("user_id_list");
                                if (userIDs == null) {
                                    // Array field is empty, create a new array with the current user ID
                                    partyRoomRef.update("user_id_list", Arrays.asList(getCurrentUserID()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    incrementPartyCapacity();
                                                    loadingDialog.dismisDialog();
                                                    Toast.makeText(JoinParty.this, "Joined Party Room Successfully!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loadingDialog.dismisDialog();
                                                    Toast.makeText(JoinParty.this, "Server Error", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else if (userIDs instanceof ArrayList) {
                                    // Array field has existing user IDs, use arrayUnion to add the current user ID
                                    partyRoomRef.update("user_id_list", FieldValue.arrayUnion(getCurrentUserID()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    incrementPartyCapacity();
                                                    loadingDialog.dismisDialog();
                                                    Toast.makeText(JoinParty.this, "Joined Party Room Successfully!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loadingDialog.dismisDialog();
                                                    Toast.makeText(JoinParty.this, "Server Error", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    loadingDialog.dismisDialog();
                                    Toast.makeText(JoinParty.this, "Invalid user_id_list field", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loadingDialog.dismisDialog();
                                Toast.makeText(JoinParty.this, "Such Party Doesn't Exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loadingDialog.dismisDialog();
                            Toast.makeText(JoinParty.this, "Fail to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
    private void incrementPartyCapacity() {
        DocumentReference partyRef = mFireStore.collection("parties").document(documentID);
        partyRef.update("party_current_capacity", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Party capacity updated successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update party capacity
                    }
                });
    }
    public void fireStoreForm(){
        loadingDialog.startLoadingDialog();
        mFireStore.collection("parties")
                .document(documentID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            document = task.getResult();
                            if (document.exists()) {
                                setPartyDetails();

                            } else {
                                Toast.makeText(JoinParty.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(JoinParty.this, "Fail to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setPartyDetails(){
        loadingDialog.dismisDialog();
        tv_item_name = findViewById(R.id.tv_item_name);
        iv_party_image = findViewById(R.id.iv_party_image);

        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_total_people = findViewById(R.id.tv_total_people);
        tv_current_people = findViewById(R.id.tv_current_people);
        tv_address = findViewById(R.id.tv_party_address);
        tv_party_category = findViewById(R.id.tv_party_category);
        tv_city_status = findViewById(R.id.tv_party_city_state);
        tv_type = findViewById(R.id.tv_type);



        Glide.with(this)
                .load(document.getString("party_img"))
                .into(iv_party_image);

        tv_item_name.setText(document.getString("party_name"));

        tv_date.setText(document.getString("party_date"));
        tv_time.setText(document.getString("party_time"));
        tv_total_people.setText(String.valueOf(document.get("party_max_capacity")));
        tv_current_people.setText(String.valueOf(document.get("party_current_capacity")));
        tv_address.setText(document.getString("party_address"));
        tv_city_status.setText(document.getString("party_city") + ", " + document.getString("party_state"));
        tv_party_category.setText(document.getString("party_category"));
        tv_type.setText(document.getString("party_type"));

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