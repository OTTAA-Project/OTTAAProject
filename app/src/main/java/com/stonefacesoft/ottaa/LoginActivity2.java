package com.stonefacesoft.ottaa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.IntentCode;

import static com.stonefacesoft.ottaa.utils.Constants.RC_SIGN_IN;

//Code source https://developers.google.com/identity/sign-in/android/sign-in

public class LoginActivity2 extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    //UI elements
    private TextView textViewLoginBig;
    private TextView textViewLoginSmall;
    private CardView cardViewLogin;
    private ImageView orangeBanner;
    private com.google.android.gms.common.SignInButton signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO hacer que sea fullscreen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_1);

        bindUI();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        animateEntrance();

    }

    private void bindUI(){
        cardViewLogin = findViewById(R.id.cardViewLogin);
        signInButton = findViewById(R.id.googleSignInButton);
        signInButton.setVisibility(View.INVISIBLE);
        signInButton.setOnClickListener(this);
        textViewLoginBig = findViewById(R.id.textLoginBig);
        textViewLoginBig.setVisibility(View.INVISIBLE);
        textViewLoginSmall = findViewById(R.id.textLoginSmall);
        textViewLoginSmall.setVisibility(View.INVISIBLE);
        orangeBanner = findViewById(R.id.orangeBanner);
        orangeBanner.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSignInButton:
                //signIn();
                animateTransition();
                break;
                //TODO put an easter egg when you click the Bubas
        }
    }

    private void signIn() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        IntentCode.PLAY_SERVICES_RESOLUTION_REQUEST.getCode()).show();
            } else {
                alertCheckPlayService();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, Authenticate with Firebase now.
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, R.string.problema_inet, Toast.LENGTH_SHORT).show();
        }
    }

    private void alertCheckPlayService() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(getResources().getString(R.string.pref_important_alert));
        dialogo1.setMessage(getResources().getString(R.string.pref_error_312));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                String url = "https://play.google.com/store/apps/details?id=com.google.android.gms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "onComplete: firebaseAuth succesfull");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //TODO Save user data if needed
                            animateTransition();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "onComplete: firebaseAuth fail");
                            Toast.makeText(LoginActivity2.this, R.string.problema_sign_in,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private  void animateEntrance(){
        //Alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setRepeatMode(Animation.ABSOLUTE);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        //CardView animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewLoginBig.startAnimation(alphaAnimation);
                textViewLoginSmall.startAnimation(alphaAnimation);
                signInButton.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        cardViewLogin.startAnimation(translateAnimation);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 700, 0);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        orangeBanner.startAnimation(translateAnimation2);

    }

    private void animateTransition(){
        //CardView animation
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 900);
        translateAnimation.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(1500);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //TODO check we dont need to send some information to the screen
                Log.d(TAG, "onAnimationEnd: Haciendo el intent");
                Intent intent = new Intent(LoginActivity2.this, LoginActivity2Step2.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cardViewLogin.startAnimation(translateAnimation);

        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 0, 900);
        translateAnimation2.setRepeatMode(Animation.ABSOLUTE);
        translateAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation2.setDuration(1000);
        translateAnimation2.setFillAfter(true);
        orangeBanner.startAnimation(translateAnimation2);
        textViewLoginBig.startAnimation(translateAnimation2);
        textViewLoginSmall.startAnimation(translateAnimation2);

    }
}
