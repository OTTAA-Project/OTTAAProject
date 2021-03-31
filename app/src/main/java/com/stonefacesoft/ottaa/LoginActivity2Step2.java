package com.stonefacesoft.ottaa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity2Step2 extends Activity implements View.OnClickListener {
    private static final String TAG = "LoginActivity2Step2";

    //UI elemetns
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    EditText editTextName;

    //User variables
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO hacer que sea fullscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_2);

        mAuth = FirebaseAuth.getInstance();

        bindUI();

        animateEntrance();

        fillUserData();

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

        editTextName = findViewById(R.id.editTextName);


    }

    @Override
    protected void onStart() {
        super.onStart();

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
                //TODO save user data on preferences and on Firebase
                Intent intent = new Intent(LoginActivity2Step2.this, LoginActivity2Step3.class);
                startActivity(intent);
                break;
            case R.id.backButton:
                Intent intent2 = new Intent(LoginActivity2Step2.this, LoginActivity2Step2.class);
                startActivity(intent2);
        }
    }

    private void fillUserData(){
        if (mAuth.getCurrentUser() != null){
//            TODO get user data, birthdate and gender is quite tricky. Probably best to ask the user to insert it
            //editTextName.setText(mAuth.getCurrentUser().getDisplayName());

        }
    }
}
