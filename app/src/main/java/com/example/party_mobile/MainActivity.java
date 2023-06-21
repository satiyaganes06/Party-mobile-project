package com.example.party_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.party_mobile.Firebase_Model.PartyDetailsModel;
import com.example.party_mobile.Utility.LoadingDialog;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    LoadingDialog loadingDialog = new LoadingDialog(this);
    private RecyclerView recycleview;
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

    private FirestoreRecyclerAdapter adapters;
    private Button btn_add_party,btn_profile,btn_JoinParty, btn_feedback;

    private ArrayList<PartyDetailsModel> mPartyDetailsListItems;

    TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        setTitle("Public Events/Parties ");

        recycleview = findViewById(R.id.recycleview);

        btn_add_party = findViewById(R.id.btn_add_party);
        btn_profile = findViewById(R.id.btn_profile);
        btn_JoinParty = findViewById(R.id.btn_JoinParty);
        btn_feedback = findViewById(R.id.btn_feedback);

        btn_JoinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), qr_scanner.class);
                startActivity(intent);
            }
        });

        btn_add_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayMyListParties.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), user_profile.class);
                startActivity(intent);
            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog.startLoadingDialog();
        displayFunction();
    }

    public void displayFunction(){
        Query query = mFireStore.collection("parties")
                .whereEqualTo("party_type", "Public");
        FirestoreRecyclerOptions<PartyDetailsModel> options = new FirestoreRecyclerOptions.Builder<PartyDetailsModel>()
                .setQuery(query, PartyDetailsModel.class)
                .build();
            loadingDialog.dismisDialog();
        adapters = new FirestoreRecyclerAdapter<PartyDetailsModel, MainActivity.PartyViewHolder>(options) {
            @NonNull
            @Override
            public MainActivity.PartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.publicparty_list, parent, false);
                return new MainActivity.PartyViewHolder(root);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainActivity.PartyViewHolder holder, int position, @NonNull PartyDetailsModel model) {
                holder.listName.setText(model.getParty_name() + " ("+ model.getParty_type() + ")");
                holder.listMaxPeople.setText(" / " + String.valueOf(model.getParty_max_capacity() + " People"));
                holder.listCurrentPeople.setText(String.valueOf(model.getParty_current_capacity()));
                holder.listDate.setText(model.getParty_date());
                holder.listime.setText(model.getParty_time());


                Glide.with(getApplicationContext())
                        .load(model.getParty_img())
                        .into(holder.image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), JoinParty.class);
                        intent.putExtra("party_id", model.getParty_id());
                        startActivity(intent);
                    }
                });
            }
        };

        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycleview.setAdapter(adapters);

        adapters.startListening();
    }

    private class PartyViewHolder extends RecyclerView.ViewHolder{

        private TextView listName;
        private TextView listMaxPeople, listCurrentPeople, listDate, listime;
        private ImageView image;

        private  Button viewParty;

        public PartyViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.tv_item_name);
            listCurrentPeople = itemView.findViewById(R.id.tv_current_people);
            listMaxPeople = itemView.findViewById(R.id.tv_item_max_people);
            image = itemView.findViewById(R.id.iv_item_image);
            listDate = itemView.findViewById(R.id.tv_date);
            listime = itemView.findViewById(R.id.tv_time);

        }

    }

}