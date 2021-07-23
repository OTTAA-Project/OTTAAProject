package com.stonefacesoft.ottaa.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;

public class AvatarUtils {
    private String TAG ="";
    private ImageView imageViewAvatar;
    private Context mContext;
    private DatabaseReference childDatabase;
    private FirebaseAuth mAuth;

    public AvatarUtils(Context mContext,ImageView imageViewAvatar,FirebaseAuth mAuth){
        this.mContext = mContext;
        this.imageViewAvatar = imageViewAvatar;
        this.mAuth = mAuth;
    }

    private final ValueEventListener firebaseEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if (snapshot.hasChild("url_foto")) {
                Log.d(TAG, "onDataChange:" + snapshot.child("url_foto").toString());
                Glide.with(mContext).load(Uri.parse(snapshot.child("url_foto").getValue().toString())).override(100,100).into(imageViewAvatar);
            } else if (snapshot.exists()) {
                String name = snapshot.getValue().toString();
                Log.d(TAG, "onDataChange:" + name);
                name = name.replace("avatar", "ic_avatar");
                setAvatarByName(mContext, name);
            } else {
                setAvatarByName(mContext, "ic_avatar11");

            }
            childDatabase.removeEventListener(firebaseEventListener);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void setAvatarByName(Context mContext, String name) {
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName()));
        Glide.with(mContext).load(drawable).override(100,100).into(imageViewAvatar);
    }
    public void getFirebaseAvatar() {
        ConnectionDetector connectionDetector = new ConnectionDetector(mContext);
        if (connectionDetector.isConnectedToInternet()) {
            setAvatarByName(mContext, "ic_avatar11");
            FirebaseUtils.getInstance().setmContext(mContext);
            childDatabase = FirebaseUtils.getInstance().getmDatabase().child(Constants.AVATAR).child(mAuth.getCurrentUser().getUid());
            childDatabase.addListenerForSingleValueEvent(firebaseEventListener);
        } else {
            setAvatarByName(mContext, "ic_avatar35");
        }
    }
}
