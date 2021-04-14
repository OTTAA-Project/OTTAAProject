package com.stonefacesoft.ottaa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stonefacesoft.ottaa.Dialogos.NewDialogsOTTAA;
import com.stonefacesoft.ottaa.Viewpagers.Viewpager_tutorial;
import com.stonefacesoft.ottaa.utils.InmersiveMode;

public class LoginActivity2Step3 extends AppCompatActivity implements View.OnClickListener {

    //UI elements
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    Button buttonTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

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
        buttonTutorial = findViewById(R.id.buttonTutorial);
        buttonTutorial.setOnClickListener(this);





    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.nextButton) {
            Intent intent = new Intent(LoginActivity2Step3.this, LoginActivity2Avatar.class);
            startActivity(intent);
        } else if (id == R.id.backButton) {
            Intent intent2 = new Intent(LoginActivity2Step3.this, LoginActivity2Step2.class);
            startActivity(intent2);
        } else if (id == R.id.buttonTutorial) {
            Intent intent3 = new Intent(LoginActivity2Step3.this, Viewpager_tutorial.class);
            startActivity(intent3);
        } else if (id == R.id.buttonAutoWorkshop) {
            new NewDialogsOTTAA(this).showAutoWorkshopDialog();
        } else if (id == R.id.buttonBookDemo) {
            new NewDialogsOTTAA(this).showBookDemoDialog();
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