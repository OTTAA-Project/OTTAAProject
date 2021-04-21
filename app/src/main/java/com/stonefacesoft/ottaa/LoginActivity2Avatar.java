package com.stonefacesoft.ottaa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.ottaa.utils.InmersiveMode;
import com.stonefacesoft.ottaa.utils.IntentCode;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity2Avatar extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivityAvatar";
    //UI elemetns
    ImageView imageViewOrangeBanner;
    ImageView imageViewThreePeople;
    ImageView imageViewAvatar;
    private final ValueEventListener firebaseEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Context mContext = LoginActivity2Avatar.this;
            if (snapshot.hasChild("url_foto")) {
                Log.d(TAG, "onDataChange:" + snapshot.child("url_foto").toString());
                Glide.with(mContext).load(Uri.parse(snapshot.child("url_foto").getValue().toString())).into(imageViewAvatar);
            } else if (snapshot.exists()) {
                String name = snapshot.getValue().toString();
                Log.d(TAG, "onDataChange:" + name);
                name = name.replace("avatar", "ic_avatar");
                setAvatarByName(mContext,name);
            } else {
                setAvatarByName(mContext,"ic_avatar11");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    TextView textViewLoginBig;
    TextView textViewLoginSmall;
    Button buttonNext;
    Button buttonPrevious;
    ImageButton imageButtonAvatar11;
    ImageButton imageButtonSelectAvatarSource;
    ConstraintLayout constraintSourceButtons;
    //User variables
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private int avatarId;
    private String avatarName;
    private boolean uploadAvatar;
    private DatabaseReference childDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new InmersiveMode(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_avatar);

        mAuth = FirebaseAuth.getInstance();

        bindUI();

        animateEntrance();
    }

    private void bindUI() {
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
        getFirebaseAvatar();


    }

    private void animateEntrance() {
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
            //TODO El proceso es lento para ir a principal avisar al usuario que esta yendo a principal
            buttonNext.setEnabled(false);
            if (uploadAvatar) {
                if (avatarId == -1) {
                    uploadFirebaseAvatar();
                } else {
                    uploadFirebaseAvatarName();
                }
            }
            Intent intent = new Intent(LoginActivity2Avatar.this, Principal.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.backButton) {
            Intent intent2 = new Intent(LoginActivity2Avatar.this, LoginActivity2Step3.class);
            startActivity(intent2);
        } else if (id == R.id.buttonSelectAvatarSource) {
            //Scale Animation to show the other buttons
            doScaleAnimation();
        } else if (id == R.id.buttonSourceCamera) {
            //TODO Quality is low, maybe use a different approch, also for EditPicto

            takePictureSetup();
        } else if (id == R.id.buttonSourceGallery) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, IntentCode.PICK_IMAGE.getCode());
        } else {
            if (view instanceof ImageView) {
                uploadAvatar = true;
                avatarId = id;
                avatarName = view.getTag().toString();
                Log.d(TAG, "onClick: " + avatarName);
                Glide.with(this).load(((ImageView) view).getDrawable()).into(imageViewAvatar);
            }
        }
    }

    private void doScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f);
        scaleAnimation.setRepeatMode(Animation.ABSOLUTE);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);
        constraintSourceButtons.setVisibility(View.VISIBLE);
        constraintSourceButtons.startAnimation(scaleAnimation);
    }

    //source https://stackoverflow.com/questions/8017374/how-to-pass-a-uri-to-an-intent

    /**
     * this method receiving a result from this activity or another
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: Cropped Image: ");
            Uri uri = data.getParcelableExtra("imageUri");
            avatarId = -1;
            uploadAvatar = true;
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
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == IntentCode.CAMARA.getCode()) {
            Log.d(TAG, "onActivityResult: Camera image done");
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("cam", "jpg");
                    FileOutputStream fosTemp = new FileOutputStream(tempFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fosTemp);
                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: ERROR saving bitmap");
                }
                Intent intent = new Intent(LoginActivity2Avatar.this, PictureCropper.class);
                intent.putExtra("pickedImageUri", Uri.fromFile(tempFile));
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            } else {
                Toast.makeText(this, "You didn't take a picture", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * this method call the camera function
     */
    private void takePictureSetup() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IntentCode.CAMARA.getCode());
        }
    }

    /**
     * this method upload  to  firebase the selected avatar picture
     */
    private void uploadFirebaseAvatar() {
        FirebaseUtils firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setmContext(this);
        final String name = System.currentTimeMillis() + ".png";
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("Archivos_Usuarios").child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).child(name);
        Bitmap bitmap = ((BitmapDrawable) imageViewAvatar.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        reference.putBytes(byteArray).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @NonNull
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                    throw task.getException();
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();
                Map<String, Object> avatar = new HashMap<>();
                String url = downloadUri.toString();
                avatar.put("name", name);
                avatar.put("url_foto", url);
                firebaseUtils.getmDatabase().child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).updateChildren(avatar);
            }
        });

    }

    /**
     * this method upload  to  firebase the selected avatar name
     */
    private void uploadFirebaseAvatarName() {
        FirebaseUtils utils = FirebaseUtils.getInstance();
        utils.setmContext(this);
        FirebaseUtils.getInstance().getmDatabase().child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid()).setValue(avatarName);
    }

    /**
     * This method return the user avatar
     */
    public void getFirebaseAvatar() {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectedToInternet()) {
            childDatabase = FirebaseUtils.getInstance().getmDatabase().child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid());
            childDatabase.addListenerForSingleValueEvent(firebaseEventListener);
        }else{
            setAvatarByName(this,"ic_avatar11");
        }
    }
    /**
     * this method shows the avatar picture on the ImageViewAvatar
     * */
    public void setAvatarByName(Context mContext,String name){
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName()));
        Glide.with(mContext).load(drawable).into(imageViewAvatar);
    }


}