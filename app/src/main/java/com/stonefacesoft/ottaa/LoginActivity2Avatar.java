package com.stonefacesoft.ottaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        new InmersiveMode(this);

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
            Intent intent2 = new Intent(LoginActivity2Avatar.this, LoginActivity2Step3.class);
            startActivity(intent2);
        } else if (id == R.id.buttonSelectAvatarSource) {
            //Scale Animation to show the other buttons
            doScaleAnimation();
        }
        else if (id == R.id.buttonSourceCamera) {
            //TODO Quality is low, maybe use a different approch, also for EditPicto
            takePictureSetup();
        }
        else if (id == R.id.buttonSourceGallery) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, IntentCode.PICK_IMAGE.getCode());
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

    //source https://stackoverflow.com/questions/8017374/how-to-pass-a-uri-to-an-intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: Cropped Image: ");
            Uri uri = data.getParcelableExtra("imageUri");
            Glide.with(this).load(uri)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .error(R.drawable.ic_no)
                    .circleCrop()
                    .into(imageViewAvatar);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Log.d(TAG, "onActivityResult: Canceled by user");

        }
        //Source https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
        if (requestCode == IntentCode.PICK_IMAGE.getCode()) {
            Log.d(TAG, "onActivityResult: Pick image done");
            if (resultCode == RESULT_OK) {
                final Uri pickedImageUri = data.getData();
                Intent intent = new Intent(LoginActivity2Avatar.this, PictureCropper.class);
                intent.putExtra("pickedImageUri", pickedImageUri);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }else {
                Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == IntentCode.CAMARA.getCode()) {
            Log.d(TAG, "onActivityResult: Camera image done");
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("cam","jpg");
                    FileOutputStream fosTemp = new FileOutputStream(tempFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fosTemp);
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: ERROR saving bitmap");
                }
                Intent intent = new Intent(LoginActivity2Avatar.this, PictureCropper.class);
                intent.putExtra("pickedImageUri", Uri.fromFile(tempFile));
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }else {
                Toast.makeText(this, "You didn't take a picture",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void takePictureSetup() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IntentCode.CAMARA.getCode());
        }
    }

}