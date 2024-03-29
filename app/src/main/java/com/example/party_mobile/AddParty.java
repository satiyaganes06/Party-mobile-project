package com.example.party_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.party_mobile.AppPermissions.AppPermissions;
import com.example.party_mobile.Constant.AllConstant;
import com.example.party_mobile.Firebase_Model.PartyDetailsModel;
import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import org.checkerframework.checker.units.qual.A;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class AddParty extends BaseActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Uri imageUri;
    private Boolean success;
    Context context;
    private AppPermissions appPermissions;
    private String email;
    private StorageReference storageReference;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    LinearLayout layout;

    private ImageView imgPick, iv_party_image;
    //Date Picker
    private TextView et_date_party, et_time_party;
    DatePickerDialog datePickerDialog;
    private EditText et_party_name, et_tel_num, et_capacity, et_address, et_city, et_state;
    private Spinner spn_part_category ,spn_party_type;
    private String store_category;
    private String store_type;
    public Integer number;

    private Button btn_submit;


    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        layout = (LinearLayout) findViewById(R.id.layout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String email = firebaseAuth.getCurrentUser().getEmail();
        storageReference = FirebaseStorage.getInstance().getReference();
        appPermissions = new AppPermissions();

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_party_name = (EditText) findViewById(R.id.et_party_name);
        spn_part_category = (Spinner) findViewById(R.id.dropdown_party_category);
        spn_party_type = (Spinner) findViewById(R.id.dropdown_party_type);
        et_tel_num = (EditText) findViewById(R.id.et_tel_num);
        et_capacity = (EditText) findViewById(R.id.et_party_capacity);
        et_address = (EditText) findViewById(R.id.et_party_address);
        et_city = (EditText) findViewById(R.id.et_party_city);
        et_state =   (EditText) findViewById(R.id.et_party_state);

        //Date
        et_date_party = findViewById(R.id.tv_date);
        et_date_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddParty.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_date_party.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //Time
        et_time_party = findViewById(R.id.tv_time);
        et_time_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddParty.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_time_party.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btn_submit = (Button) findViewById(R.id.btn_Submit);

        imgPick = (ImageView) findViewById(R.id.iv_add_update_image);
        iv_party_image = (ImageView) findViewById(R.id.iv_party_image);

        imgPick.setOnClickListener(view -> {
            if (appPermissions.isStorageOk(this)) {
                pickImage();
            } else {
                appPermissions.requestStoragePermission(this);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), email.toString(), Toast.LENGTH_LONG);

                validation(email);

            }
        });

        selectCategorySpinner();
        selectTypeSpinner();
    }

    private void pickImage() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    void validation(String email){

        if(et_party_name.getText().toString().isEmpty()){
            snackbar("Please enter party name", layout);

        }else if(String.valueOf(spn_part_category.getSelectedItem()).equals("--Party Category--")){
            snackbar("Please select category", layout);

        }else if(String.valueOf(spn_party_type.getSelectedItem()).equals("--Part Type--")){
            snackbar("Please select type", layout);

        }else if(et_tel_num.getText().toString().isEmpty()){
            snackbar("Please enter phone number", layout);

        }else if(et_date_party.getText().toString().isEmpty()){
            snackbar("Please select party date", layout);

        }else if(et_time_party.getText().toString().isEmpty()){
            snackbar("Please select party time", layout);

        }else if(et_capacity.getText().toString().isEmpty()){
            snackbar("Please enter max capacity", layout);

        }else if(et_address.getText().toString().isEmpty()) {
            snackbar("Please enter address", layout);

        }else if(et_city.getText().toString().isEmpty()) {
                snackbar("Please enter city", layout);

        }else if(et_state.getText().toString().isEmpty()) {
                    snackbar("Please enter state", layout);
        }else{
           loadingDialog.startLoadingDialog();
           imageUpload(email);

        }
    }

    private void imageUpload(String email){

        storageReference.child("Party Image/"+ System.currentTimeMillis() + "_" + "party").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> image = taskSnapshot.getStorage().getDownloadUrl();
                image.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Uri> imageTask) {

                        if (imageTask.isSuccessful()) {

                            String url = imageTask.getResult().toString();
                            uploadPartyInfo(email, url);

                        } else {
                            Log.d("TAG", "onComplete: Image Path" + imageTask.getException());
                            loadingDialog.dismisDialog();
                        }
                    }
                });

            }
        });
    }

    private void uploadPartyInfo(String email, String imageUrl){
        PartyDetailsModel partyDetails;
        String user_id = getCurrentUserID();
        String documentID = generateRandomID();
        String party_join_code = generatePartyCode();


        partyDetails = new PartyDetailsModel(documentID, getCurrentUserID(), et_party_name.getText().toString(), store_category,
                store_type, et_tel_num.getText().toString(), et_date_party.getText().toString(), et_time_party.getText().toString(),
                Integer.parseInt(et_capacity.getText().toString()), 0, et_address.getText().toString(), et_city.getText().toString(),
                et_state.getText().toString(), imageUrl, party_join_code);


        mFireStore.collection("parties")
                .document(documentID)
                .set(partyDetails, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createPartyRoom(party_join_code, documentID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismisDialog();
                        Toast.makeText(AddParty.this, "Server Error", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void createPartyRoom(String party_join_code, String documentID){
        mFireStore.collection("partyRoom")
                        .document(documentID)
                        .set(new HashMap<String, Object>() {
                            {
                                put("party_id", documentID);
                                put("party_join_code", party_join_code);
                                put("user_id_list", Arrays.asList());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingDialog.dismisDialog();
                                Toast.makeText(AddParty.this, "Party was added successfully !!!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismisDialog();
                                Toast.makeText(AddParty.this, "Server Error", Toast.LENGTH_LONG).show();
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

    public static String generatePartyCode() {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < 4; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Glide.with(this).load(imageUri).into(iv_party_image);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
                Log.d("TAG", "onActivityResult: " + exception);
            }
        }

        if(requestCode==100){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                boolean value = Settings.System.canWrite(getApplicationContext());
                if(value){
                    success = true;
                }else{
                    Toast.makeText(context, "Permission is not granted", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AllConstant.STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(this, "Storage permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void selectCategorySpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.party_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_part_category.setAdapter(adapter);

        spn_part_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                AddParty.this.number = spn_part_category
                        .getSelectedItemPosition() + 1;

                store_category = spn_part_category.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

    }

    public void selectTypeSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.party_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_party_type.setAdapter(adapter);

        spn_party_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                AddParty.this.number = spn_party_type
                        .getSelectedItemPosition() + 1;

                store_type = spn_party_type.getItemAtPosition(position).toString();

            }

            public void onNothingSelected(AdapterView<?> arg0) {// do nothing
            }

        });

    }
}