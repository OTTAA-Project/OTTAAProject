package com.stonefacesoft.ottaa.utils.UserLicence;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stonefacesoft.ottaa.FirebaseRequests.FirebaseUtils;
import com.stonefacesoft.ottaa.LoginActivity2;
import com.stonefacesoft.ottaa.utils.constants.Constants;
import com.stonefacesoft.ottaa.utils.preferences.User;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Gonzalo Juarez y Gaston saillen
 *
 * <h3>Objetive</h3>
 * Analize if the user pay or not.
 */
public class LicenciaUsuario {
    private static final String TAG = "LicenciaUsuario";
    private final DatabaseReference databaseReference;
    private final FirebaseAuth mAuth;
    private final Context mContext;
    private final FirebaseUtils firebaseUtils;



    public LicenciaUsuario(Context mContext) {
        this.mContext = mContext;
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setmContext(mContext);
        firebaseUtils.setUpFirebaseDatabase();

        this.mAuth = FirebaseAuth.getInstance();
        this.databaseReference = firebaseUtils.getmDatabase();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    new VerificarPagoUsuario(firebaseAuth.getUid()).execute();
                } else {
                    if (mContext != null) {
                        Intent intent = new Intent(mContext, LoginActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
    }

    private void changePremiumState(String valor) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt(Constants.PREMIUM, Integer.parseInt(valor)).apply();
    }

    private class VerificarPagoUsuario extends AsyncTask<Void, Void, Void> {
        //SAF_GTG: SubirArchivosFirebase_getTiempoGoogle
        private final String uid;
        private String dateStr;

        public VerificarPagoUsuario(String uid) {
            this.uid = uid;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = new URL("https://google.com");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
               if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    dateStr = urlConnection.getHeaderField("Date");
                    Date startDate = df.parse(dateStr);
                    dateStr = String.valueOf(startDate.getTime() / 1000);
                    Long horaActual = java.lang.Long.parseLong(dateStr);
                        databaseReference.child(Constants.PAGO).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = User.getInstance();
                                    if(user==null)
                                        user = User.getInstance(mContext);
                                String uid = User.getInstance().getUserUid(mContext);
                                if(!uid.isEmpty()&&snapshot.hasChild(uid)){
                                    snapshot.child(uid).getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            processDataSnapshot(dataSnapshot,horaActual);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                } else {
                    //Closes the connection.
                    throw new IOException(urlConnection.getResponseMessage());
                }
                urlConnection.disconnect();
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Error: " + e.getMessage());
            } catch (ParseException e) {
                Log.e(TAG, "doInBackground: Error" + e.getMessage());
            }catch (Exception ex){

            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute() {

        }
    }
    private void processDataSnapshot(DataSnapshot dataSnapshot,Long date){
        if (!dataSnapshot.hasChild(Constants.FECHAPAGO)) {
            snapshotIsEmpty(date);
        } else if (dataSnapshot.hasChild(Constants.FECHAVENCIMIENTO)) {
            snapShotWithExpiredDate(dataSnapshot,date);
        } else if (dataSnapshot.hasChild(Constants.FECHAPAGO)) {
            snapshotWithPaymentDate(dataSnapshot,date);
        }
    }

    private void snapshotIsEmpty(Long date){
        String uid = User.getInstance(mContext).getUserUid();
        databaseReference.child(Constants.PRIMERACONEXION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(uid)){
                    snapshot.child(uid).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(Constants.PRIMERACONEXION)) {
                                Long primeraConexion = Long.parseLong(snapshot.child(Constants.PRIMERACONEXION).getValue().toString());
                                if (date.compareTo(primeraConexion) > 0) {
                                    changePremiumState(1 + "");
                                    databaseReference.child(Constants.PAGO).child(mAuth.getCurrentUser().getUid()).child(Constants.PAGO).getRef().setValue(1);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void snapShotWithExpiredDate(DataSnapshot dataSnapshot,Long date){
        Long tiempoPago = Long.parseLong(dataSnapshot.child(Constants.FECHAVENCIMIENTO).getValue().toString());
        dataSnapshot.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int result = date.compareTo(tiempoPago);
                if(dataSnapshot.hasChild(Constants.PAGO)){
                    if(result >0 || dataSnapshot.child(Constants.PAGO).getValue().toString().contains("0")){
                        dataSnapshot.child(Constants.PAGO).getRef().setValue(1);
                        changePremiumState(1+"");
                    }
                    else if(dataSnapshot.child(Constants.PAGO).getValue().toString().contains("1")){
                        changePremiumState(1+"");
                    }
                }else{
                    dataSnapshot.child(Constants.PAGO).getRef().setValue(1);
                    changePremiumState(1+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void snapshotWithPaymentDate(DataSnapshot dataSnapshot,Long date){
        Long tiempoPago = Long.parseLong(dataSnapshot.child(Constants.FECHAPAGO).getValue().toString());
        dataSnapshot.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int result = date.compareTo(tiempoPago + Constants.UN_ANIO);
                if(dataSnapshot.hasChild(Constants.PAGO)){
                    if (result > 0 || dataSnapshot.child(Constants.PAGO).getValue().toString().contains("0")) {
                        dataSnapshot.child(Constants.PAGO).getRef().setValue(1);
                        changePremiumState(1 + "");
                    } else if (dataSnapshot.child(Constants.PAGO).getValue().toString().contains("1")) {
                        changePremiumState(1 + "");
                    }
                }else{
                    dataSnapshot.child(Constants.PAGO).getRef().setValue(1);
                    changePremiumState(1+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
