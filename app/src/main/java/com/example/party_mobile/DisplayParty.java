package com.example.party_mobile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class DisplayParty extends BaseActivity {
    LoadingDialog loadingDialog = new LoadingDialog(this);

    private String documentID;
    DocumentSnapshot document;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Uri imageUri;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private TextView tv_party_category,tv_type, tv_date, tv_time, tv_item_name, tv_address, tv_city,
            tv_state, tv_telNum, tv_current_people, tv_total_people;
    private ImageView iv_qr_code, iv_party_image;
    private Button btn_Update;

    private int brightness=255;
    private ContentResolver cResolver;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_party);
        setTitle("My Party Details");

        checkSystemWritePermission();
        cResolver = getContentResolver();

        //Get the current window
        window = getWindow();

        try
        {
            Settings.System.putInt(cResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {

            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }

        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

        WindowManager.LayoutParams layoutpars = window.getAttributes();
        //Set the brightness of this window
        layoutpars.screenBrightness = brightness / (float)100;
        window.setAttributes(layoutpars);

        documentID = getIntent().getStringExtra("party_id");


        fireStoreForm();
//
//        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
//
//        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        btn_Update = findViewById(R.id.btn_Update);
//
//        btn_Update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), DisplayParty.class);
//                intent.putExtra("party_ID", documentID);
//                startActivity(intent);
//            }
//        });
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
                                Toast.makeText(DisplayParty.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DisplayParty.this, "Fail to load", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setPartyDetails(){
        loadingDialog.dismisDialog();
        tv_item_name = findViewById(R.id.tv_item_name);
        iv_party_image = findViewById(R.id.iv_party_image);
        tv_telNum = findViewById(R.id.tv_telNum);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_total_people = findViewById(R.id.tv_total_people);
        tv_current_people = findViewById(R.id.tv_current_people);
        tv_address = findViewById(R.id.tv_address);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        tv_party_category = findViewById(R.id.tv_party_category);
        tv_type = findViewById(R.id.tv_type);
        iv_qr_code = findViewById(R.id.iv_qr);



        Glide.with(this)
                .load(document.getString("party_img"))
                .into(iv_party_image);

        tv_item_name.setText(document.getString("party_name"));
        tv_telNum.setText(document.getString("user_telNum"));
        tv_date.setText(document.getString("party_date"));
        tv_time.setText(document.getString("party_time"));
        tv_total_people.setText(String.valueOf(document.get("party_max_capacity")));
        tv_current_people.setText(String.valueOf(document.get("party_current_capacity")));
        tv_address.setText(document.getString("party_address"));
        tv_city.setText(document.getString("party_city"));
        tv_state.setText(document.getString("party_state"));
        tv_party_category.setText(document.getString("party_category"));
        tv_type.setText(document.getString("party_type"));
        generateQR(document.getString("party_join_code"));
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

    public void generateQR(String code){
        MultiFormatWriter multifornatliriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multifornatliriter.encode(code.toString(), BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qr_code.setImageBitmap(bitmap);
        }catch (WriterException e){
                throw new RuntimeException(e);
        }
    }

    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d(TAG, "Can Write Settings: " + retVal);
            if(retVal){
                Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Write not allowed :-(", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
        }
        return retVal;
    }
}