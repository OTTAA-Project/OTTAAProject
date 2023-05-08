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

import com.stonefacesoft.ottaa.Dialogos.newsDialog.NewDialogsOTTAA;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.Viewpagers.Viewpager_tutorial;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.CustomToast;
import com.stonefacesoft.ottaa.utils.Firebase.AnalyticsFirebase;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.RemoteConfigUtils;

public class LoginActivity2Step3 extends AppCompatActivity implements View.OnClickListener {

    //UI elements
    private ImageView imageViewOrangeBanner;
    private ImageView imageViewThreePeople;
    private TextView textViewLoginBig;
    private TextView textViewLoginSmall;
    private Button buttonNext;
    private Button buttonPrevious;
    private Button buttonTutorial;
    private Button buttonBookDemo;
    private Button buttonAutoworkshop;
    private boolean comingFromMainActivity;
    private ConnectionDetector connectionDetector;
    private AnalyticsFirebase mAnalyticsFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intento = getIntent();
        if(intento!=null){
            if(intento.hasExtra("comingFromMainActivity"))
                comingFromMainActivity = true;
        }
        connectionDetector = new ConnectionDetector(this);
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_3);
        mAnalyticsFirebase=new AnalyticsFirebase(this);

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
        buttonAutoworkshop = findViewById(R.id.buttonAutoWorkshop);
        buttonAutoworkshop.setOnClickListener(this);
        buttonBookDemo = findViewById(R.id.buttonBookDemo);
        buttonBookDemo.setVisibility(View.GONE);
        boolean book = RemoteConfigUtils.getInstance().getBooleanByName("BookADemo");
        if(book){
            buttonBookDemo.setVisibility(View.VISIBLE);
        }
        buttonBookDemo.setOnClickListener(this);
        if(comingFromMainActivity){
            buttonPrevious.setVisibility(View.INVISIBLE);
        }

        if(comingFromMainActivity){
            buttonPrevious.setVisibility(View.INVISIBLE);
            buttonNext.setText(getResources().getText(R.string.exit_general));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.nextButton:
                if(!comingFromMainActivity){
                    mAnalyticsFirebase.customEvents("Touch","LoginActivityStep3","Next2");
                    Intent intent = new Intent(LoginActivity2Step3.this, LoginActivity2Avatar.class);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }
                break;
            case R.id.backButton:
                mAnalyticsFirebase.customEvents("Touch","LoginActivityStep3","Back2");
                    Intent intent2 = new Intent(LoginActivity2Step3.this, LoginActivity2Step2.class);
                    startActivity(intent2);
                    finish();
                break;
            case R.id.buttonTutorial:
                mAnalyticsFirebase.customEvents("Touch","LoginActivityStep3","ButtonTutorial");
                Intent intent3 = new Intent(LoginActivity2Step3.this, Viewpager_tutorial.class);
                startActivity(intent3);
                break;
            case R.id.buttonAutoWorkshop:
                if(connectionDetector.isConnectedToInternet()){
                    mAnalyticsFirebase.customEvents("Touch","LoginActivityStep3","ButtonAutoWorkShop");
                    new NewDialogsOTTAA(this).showAutoWorkshopDialog();
                }else{
                    CustomToast.getInstance(this).mostrarFrase(getResources().getText(R.string.problema_inet));
                }
                break;
            case R.id.buttonBookDemo:
                if(connectionDetector.isConnectedToInternet()){
                    mAnalyticsFirebase.customEvents("Touch","LoginActivityStep3","ButtonBookDemo");
                    new NewDialogsOTTAA(this).showBookDemoDialog();
                }else{
                    CustomToast.getInstance(this).mostrarFrase(getResources().getText(R.string.problema_inet));
                }
                break;
        }
    }

    private void animateEntrance(){
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