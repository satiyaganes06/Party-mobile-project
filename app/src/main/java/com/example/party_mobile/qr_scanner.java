package com.example.party_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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


public class qr_scanner extends BaseActivity implements SensorEventListener {


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 20.0f; // Adjust this value according to sensitivity
    private static final int SHAKE_TIME_INTERVAL = 1000; // Adjust this value according to sensitivity
    private long lastShakeTime = 0;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        btnScan = findViewById(R.id.btn_Scan);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Register the accelerometer sensor listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the accelerometer sensor listener
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastShakeTime) > SHAKE_TIME_INTERVAL) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(x * x + y * y + z * z);

                if (acceleration > SHAKE_THRESHOLD) {
                    lastShakeTime = currentTime;
                    scanCode();
                }
            }
        }
    }
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan the QR Code");
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
                                 String partyID = document.getString("party_id");
                                 Intent intent = new Intent(getApplicationContext(), JoinParty.class);
                                 intent.putExtra("party_id", partyID);
                                 startActivity(intent);
                                 // Do something with the retrieved data
//                                 AlertDialog.Builder builder = new AlertDialog.Builder(qr_scanner.this);
//                                 builder.setTitle("Result");
//                                 builder.setMessage("Party Name: " + partyName);
//                                 builder.setPositiveButton("Ok", (dialogInterface, i) -> {
//                                     dialogInterface.dismiss();
//                                 }).show();
                             }
                         } else {
                             // Handle any errors that occurred
                         }
                     });
         }

     });



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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