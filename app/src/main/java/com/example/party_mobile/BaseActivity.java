package com.example.party_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.party_mobile.Utility.LoadingDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity{



    public void showErrorSnackBar(String message , Boolean errorMessage){
        Snackbar snackBar = Snackbar.make(findViewById(androidx.constraintlayout.widget.R.id.wrap_content_constrained), message, Snackbar.LENGTH_LONG);

        View snackBarView = snackBar.getView();

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            BaseActivity.this,
                            R.color.colorSnackBarError
                    )
            );
        }else{
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            BaseActivity.this,
                            R.color.colorSnackBarSuccess
                    )
            );
        }
        snackBar.show();
    }

    public void snackbar(String error_message, LinearLayout context){
        Snackbar snackbar
                = Snackbar
                .make(findViewById(androidx.constraintlayout.widget.R.id.wrap_content_constrained), error_message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public String getCurrentUserID(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            return user.getUid();
        } else {
            return "no user";
        }
    }
}
