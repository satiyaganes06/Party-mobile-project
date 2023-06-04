package com.example.party_mobile.Utility;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.party_mobile.R;

import java.io.IOException;

public class GlideLoader {

    private Context context;

    public GlideLoader(Context context) {
        this.context = context;
    }

//    // A function to load image from URI for the user profile picture.
//    public void loadUserPicture(Object image, ImageView imageView) {
//        try {
//            // Load the user image in the ImageView.
//            Glide.with(context)
//                    .load(image) // URI of the image
//                    .centerCrop() // Scale type of the image.
//                    .placeholder(R.drawable.img_default_user) // A default place holder if image is failed to load.
//                    .into(imageView); // the view in which the image will be loaded.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void loadPartyImg(Object image, ImageView imageView) {
        // Load the user image in the ImageView.
        Glide.with(context)
                .load(image) // URI of the image
                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.image_placeholder) // A default place holder if image is failed to load.
                .into(imageView); // the view in which the image will be loaded.
    }
}

