package com.stonefacesoft.ottaa.utils.AvatarPackage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.utils.ConnectionDetector;
import com.stonefacesoft.ottaa.utils.Constants;
import com.stonefacesoft.pictogramslibrary.utils.GlideAttatcher;

import org.jetbrains.annotations.NotNull;

public class AvatarUtils {
    private String TAG ="AvatarUtils";
    private ImageView imageViewAvatar;
    private Context mContext;
    private DatabaseReference childDatabase;
    private FirebaseAuth mAuth;
    private String name;
    private GlideAttatcher glideAttatcher;

    public AvatarUtils(Context mContext,ImageView imageViewAvatar,FirebaseAuth mAuth){
        this.mContext = mContext;
        this.imageViewAvatar = imageViewAvatar;
        this.mAuth = mAuth;
        glideAttatcher = new GlideAttatcher(mContext);
    }

    private ValueEventListener firebaseChildEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            if (snapshot.hasChild("url_foto")) {
                glideAttatcher.setHeight(imageViewAvatar.getHeight()).setWidth(imageViewAvatar.getHeight()).attachedOnImaView(imageViewAvatar,Uri.parse(snapshot.child("url_foto").getValue().toString()));
            } else if (snapshot.exists()&&!snapshot.hasChild("url_foto")) {
                name = snapshot.getValue().toString().replace("avatar", "ic_avatar");
                setAvatarByName(mContext, name);
            } else {
                setAvatarByName(mContext, SelectedAvatar.getInstance().getName());
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.e(TAG, "onCancelled: "+error.getMessage());
        }

    };

    private void setAvatarByName(Context mContext, String name) {
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName()));
       glideAttatcher.useDiskCacheStrategy().setHeight(imageViewAvatar.getHeight()).setWidth(imageViewAvatar.getHeight()).attachedOnImaView(imageViewAvatar,drawable);
    }
    public void getFirebaseAvatar() {
        this.name = SelectedAvatar.getInstance().getName();
        ConnectionDetector connectionDetector = new ConnectionDetector(mContext);
        if (connectionDetector.isConnectedToInternet()) {
            FirebaseUtils.getInstance().setmContext(mContext);
            childDatabase = FirebaseUtils.getInstance().getmDatabase().child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid());
            childDatabase.addValueEventListener(firebaseChildEventListener);

        } else {
            setAvatarByName(mContext, name);
        }
    }
}
