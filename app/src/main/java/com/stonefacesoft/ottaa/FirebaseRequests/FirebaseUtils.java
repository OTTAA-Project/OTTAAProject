package com.stonefacesoft.ottaa.FirebaseRequests;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stonefacesoft.ottaa.R;

public class FirebaseUtils {
    private DatabaseReference mDatabase;
    private Context mContext;
    private static FirebaseUtils myFirebaseUtils;
    private volatile String Uid="";
    private volatile String email="";


    public synchronized static FirebaseUtils getInstance() {
        if(myFirebaseUtils==null)
            myFirebaseUtils=new FirebaseUtils();
        return myFirebaseUtils;
    }

    public FirebaseUtils setmContext(Context mContext){
        this.mContext=mContext;
        return this;
    }

    private FirebaseUtils() {
        mDatabase= FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getmDatabase(){
        return mDatabase;
    }

    public void setUpFirebaseDatabase(){
        if(mContext!=null&&!mContext.getResources().getString(R.string.firebase_name_database).isEmpty())
            mDatabase=FirebaseDatabase.getInstance(mContext.getResources().getString(R.string.firebase_name_database)).getReference();
        else if(mContext==null||mContext.getResources().getString(R.string.firebase_name_database).isEmpty())
            mDatabase=FirebaseDatabase.getInstance().getReference();
    }

    public void setUpFirebaseDatabase(String path){
        mDatabase=FirebaseDatabase.getInstance(path).getReference();
    }

    public DatabaseReference checkFirebaseNetworkConnection(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        return connectedRef;
    }

    public void setUid(String uid){
        this.Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
