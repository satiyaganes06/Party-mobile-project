package com.example.party_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.google.firebase.firestore.FirebaseFirestore;


public class qr_scanner extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        btnScan = findViewById(R.id.btn_Scan);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        setTitle("Scan Party QR");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume Up to on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
     }

     ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
         if (result.getContents() != null) {
             String scannedCode = result.getContents();

             FirebaseFirestore db = FirebaseFirestore.getInstance();
             db.collection("parties")
                     .whereEqualTo("party_join_code", scannedCode)
                     .get()
                     .addOnCompleteListener(task -> {
                         if (task.isSuccessful()) {
                             for (QueryDocumentSnapshot document : task.getResult()) {
                                 // Retrieve the data from the document
                                 // For example, if you have a field called "party_name":
                                 String partyName = document.getString("party_name");

                                 // Do something with the retrieved data
                                 AlertDialog.Builder builder = new AlertDialog.Builder(qr_scanner.this);
                                 builder.setTitle("Result");
                                 builder.setMessage("Party Name: " + partyName);
                                 builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                                     dialogInterface.dismiss();
                                 }).show();
                             }
                         } else {
                             // Handle any errors that occurred
                         }
                     });
         }

     });
}