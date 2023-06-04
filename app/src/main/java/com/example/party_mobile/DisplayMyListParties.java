package com.example.party_mobile;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.party_mobile.Firebase_Model.PartyDetailsModel;
import com.example.party_mobile.Utility.LoadingDialog;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import android.service.controls.actions.FloatAction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DisplayMyListParties extends BaseActivity {

    LoadingDialog loadingDialog = new LoadingDialog(this);
    private final FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

    private RecyclerView rv_parties;
    private FloatingActionButton btn_add_party;
    private FirestoreRecyclerAdapter adapters;


    // A global variable for the cart list items.
    private ArrayList<PartyDetailsModel> mPartyDetailsListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_parties);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        setTitle("My Parties");

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        rv_parties = findViewById(R.id.rv_parties);
        btn_add_party = findViewById(R.id.add_party);

        btn_add_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddParty.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getWishList();
        displayFunction();
    }

    public void displayFunction(){
        Query query = mFireStore.collection("parties")
                    .whereEqualTo("user_id", getCurrentUserID());
            FirestoreRecyclerOptions<PartyDetailsModel> options = new FirestoreRecyclerOptions.Builder<PartyDetailsModel>()
                    .setQuery(query, PartyDetailsModel.class)
                    .build();

            adapters = new FirestoreRecyclerAdapter<PartyDetailsModel, PartyViewHolder>(options) {
                @NonNull
                @Override
                public PartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list, parent, false);
                    return new PartyViewHolder(root);
                }

                @Override
                protected void onBindViewHolder(@NonNull PartyViewHolder holder, int position, @NonNull PartyDetailsModel model) {
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
                            Intent intent = new Intent(getApplicationContext(), DisplayParty.class);
                            intent.putExtra("party_id", model.getParty_id());
                            startActivity(intent);
                        }
                    });
                }
            };

        rv_parties.setHasFixedSize(true);
        rv_parties.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_parties.setAdapter(adapters);

        adapters.startListening();
    }

    private class PartyViewHolder extends RecyclerView.ViewHolder{

        private TextView listName;
        private TextView listMaxPeople, listCurrentPeople, listDate, listime;
        private ImageView image;

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
