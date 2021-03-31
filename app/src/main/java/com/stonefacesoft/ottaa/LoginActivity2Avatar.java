package com.stonefacesoft.ottaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2Avatar extends AppCompatActivity implements View.OnClickListener {

    //User variables
    private FirebaseAuth mAuth;

    //UI elemetns
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    ImageButton imageButtonAvatar11;
    ImageButton imageButtonAvatar12;
    ImageButton imageButtonAvatar13;
    ImageButton imageButtonAvatar14;
    ImageButton imageButtonAvatar15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO hacer que sea fullscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_avatar);

        //TODO get user data Name, Birthday and Gender
        mAuth = FirebaseAuth.getInstance();

        bindUI();

        animateEntrance();
    }

    private void bindUI(){
        imageViewOrangeBanner = findViewById(R.id.orangeBanner2);
        imageViewThreePeople = findViewById(R.id.imagen3personas);
        textViewLoginBig = findViewById(R.id.textLoginBig);
        textViewLoginSmall = findViewById(R.id.textLoginSmall);

        buttonNext = findViewById(R.id.nextButton);
        buttonNext.setOnClickListener(this);
        buttonPrevious = findViewById(R.id.backButton);
        buttonPrevious.setOnClickListener(this);

        imageButtonAvatar11 = findViewById(R.id.avatar11);
        imageButtonAvatar12 = findViewById(R.id.avatar12);
        imageButtonAvatar13 = findViewById(R.id.avatar13);


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
        switch (view.getId()){
            case R.id.nextButton:
                Intent intent = new Intent(LoginActivity2Avatar.this, Principal.class);
                startActivity(intent);
                break;
            case R.id.backButton:
                //TODO Check if backpress will do
                Intent intent2 = new Intent(LoginActivity2Avatar.this, LoginActivity2Step3.class);
                startActivity(intent2);
        }
    }

}