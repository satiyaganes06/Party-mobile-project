package com.example.party_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    TextView tv ;
    Button btn_add_party;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        btn_add_party = findViewById(R.id.btn_add_party);

        tv.setText(getCurrentUserID());

        btn_add_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayMyListParties.class);
                startActivity(intent);
            }
        });
    }
}