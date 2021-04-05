package com.stonefacesoft.ottaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2Avatar extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivityAvatar";

    //User variables
    private FirebaseAuth mAuth;

    //UI elemetns
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    ImageView imageViewAvatar;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    ImageButton imageButtonAvatar11;
    ImageButton imageButtonSelectAvatarSource;
    ConstraintLayout constraintSourceButtons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO hacer que sea fullscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_avatar);

        mAuth = FirebaseAuth.getInstance();

        bindUI();

        animateEntrance();
    }

    private void bindUI(){
        imageViewOrangeBanner = findViewById(R.id.orangeBanner2);
        imageViewThreePeople = findViewById(R.id.imagen3personas);
        imageViewAvatar = findViewById(R.id.imgAvatar);

        textViewLoginBig = findViewById(R.id.textLoginBig);
        textViewLoginSmall = findViewById(R.id.textLoginSmall);

        buttonNext = findViewById(R.id.nextButton);
        buttonNext.setOnClickListener(this);
        buttonPrevious = findViewById(R.id.backButton);
        buttonPrevious.setOnClickListener(this);

        imageButtonAvatar11 = findViewById(R.id.avatar11);
        imageButtonSelectAvatarSource = findViewById(R.id.buttonSelectAvatarSource);

        constraintSourceButtons = findViewById(R.id.constraintSourceButtons);


    }

    private  void animateEntrance(){
        TranslateAnimation translateAnimation = new TranslateAnimation(-700, 0, 0, 0);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(1000);
        translateAnimation.setFillAfter(true);
        imageViewOrangeBanner.startAnimation(translateAnimation);
        textViewLoginBig.startAnimation(translateAnimation);
        textViewLoginSmall.startAnimation(translateAnimation);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        imageViewThreePeople.startAnimation(translateAnimation2);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.nextButton) {
            //TODO Check that the avatar is choosen, if not select a default one.
            Intent intent = new Intent(LoginActivity2Avatar.this, Principal.class);
            startActivity(intent);
        } else if (id == R.id.backButton) {
            //TODO Check if backpress will do
            Intent intent2 = new Intent(LoginActivity2Avatar.this, LoginActivity2Step3.class);
            startActivity(intent2);
        } else if (id == R.id.buttonSelectAvatarSource) {
            //Scale Animation to show the other buttons
            doScaleAnimation();
        }
        else if (id == R.id.buttonSourceGallery) {
            //TODO Select an Image from Gallery
        }
        else if (id == R.id.buttonSourceCamera) {
            //TODO Take a picture from Camera, resize it and store it. Maybe do it with a CloudFunction????

        }
        else {
            if (view instanceof ImageView){
                Glide.with(this).load(((ImageView) view).getDrawable()).into(imageViewAvatar);
            }
        }
    }

    private void doScaleAnimation(){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f,1f,1f);
        scaleAnimation.setRepeatMode(Animation.ABSOLUTE);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        constraintSourceButtons.setVisibility(View.VISIBLE);
        constraintSourceButtons.startAnimation(scaleAnimation);
    }
}