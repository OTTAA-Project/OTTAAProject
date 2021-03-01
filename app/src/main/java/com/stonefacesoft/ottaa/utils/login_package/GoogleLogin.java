package com.stonefacesoft.ottaa.utils.login_package;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.IntentCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GoogleLogin extends Login_class implements View.OnClickListener{
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private com.google.android.gms.common.SignInButton SingIn;
    public GoogleLogin(AppCompatActivity mActivity) {
        super(mActivity);
        //Este es el boton para hacer el sign in, ver si lo ponemos en otro activity o donde
        SingIn = (com.google.android.gms.common.SignInButton) mActivity.findViewById(R.id.sign_in_button);
        SingIn.setEnabled(true);
        mAuth.addAuthStateListener(mAuthListener);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(this.mActivity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(mActivity, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        signIn();


    }

    private void signIn() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(mActivity);

        if (result == ConnectionResult.SUCCESS) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);

        } else {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(mActivity, result,
                        IntentCode.PLAY_SERVICES_RESOLUTION_REQUEST.getCode()).show();


            } else {
                AlertCheckPlayService();

            }
        }

    }
    private void AlertCheckPlayService() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(mActivity);
        dialogo1.setTitle(mActivity.getResources().getString(R.string.pref_important_alert));
        dialogo1.setMessage(mActivity.getResources().getString(R.string.pref_error_312));
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(mActivity.getResources().getString(R.string.pref_yes_alert), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                String url = "https://play.google.com/store/apps/details?id=com.google.android.gms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mActivity.startActivity(i);
                mActivity.finish();
            }
        });
        AlertDialog dialog = dialogo1.create();
        dialog.show();
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("LoginActivity", "firebaseAuth succesfull");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("LoginActivity", "firebaseAuth fail");
                            Toast.makeText(mActivity, R.string.problema_sign_in,
                                    Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    public SignInButton getSingIn() {
        return SingIn;
    }
}
