package com.stonefacesoft.ottaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity2Step3 extends AppCompatActivity implements View.OnClickListener {

    //UI elements
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_3);

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


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextButton:
                Intent intent = new Intent(LoginActivity2Step3.this, LoginActivity2Avatar.class);
                startActivity(intent);
                break;
            case R.id.backButton:
                Intent intent2 = new Intent(LoginActivity2Step3.this, LoginActivity2Step2.class);
                startActivity(intent2);
                break;
            case R.id.buttonTutorial:

                break;
            case R.id.buttonAutoWorkshop:
                //TODO launch a Dialog that will explain what is the Autoworkshop
                break;
            case R.id.buttonBookDemo:
                //TODO launch a Dialog that will tell how to contact us or send email with contact details.
                break;
        }
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
}