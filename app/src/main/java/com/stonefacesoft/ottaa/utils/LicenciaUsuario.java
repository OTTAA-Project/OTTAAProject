package com.stonefacesoft.ottaa.utils;

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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
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
    private String pago;
    private String FechaPago;
    private boolean licenciaActivada;
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
                    new VerificarPagoUsuario(mAuth.getCurrentUser().getUid()).execute();
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

    private void cambiarEstadoPremium(String valor) {
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
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("https://google.com");
                HttpResponse response = httpclient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    dateStr = response.getFirstHeader("Date").getValue();
                    Date startDate = df.parse(dateStr);
                    dateStr = String.valueOf(startDate.getTime() / 1000);
                    Long horaActual = java.lang.Long.parseLong(dateStr);
                    if(mAuth != null) {
                        databaseReference.child(Constants.PAGO).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChild(Constants.FECHAPAGO)) {
                                    databaseReference.child(Constants.PRIMERAULTIMACONEXION).child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(Constants.PRIMERACONEXION)) {
                                                Long primeraConexion = Long.parseLong(dataSnapshot.child(Constants.PRIMERACONEXION).getValue().toString());
                                                if (horaActual.compareTo(primeraConexion) > 0) {
                                                    cambiarEstadoPremium(0 + "");
                                                    databaseReference.child(Constants.PAGO).child(mAuth.getCurrentUser().getUid()).child(Constants.PAGO).getRef().setValue(0);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                } else if (dataSnapshot.hasChild(Constants.FECHAVENCIMIENTO)) {

                                    Long tiempoPago = Long.parseLong(dataSnapshot.child(Constants.FECHAVENCIMIENTO).getValue().toString());
                                    dataSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int result = horaActual.compareTo(tiempoPago);
                                                if(dataSnapshot.hasChild(Constants.PAGO)){
                                                    if(result >0 || dataSnapshot.child(Constants.PAGO).getValue().toString().contains("0")){
                                                        dataSnapshot.child(Constants.PAGO).getRef().setValue(0);
                                                        cambiarEstadoPremium(0+"");
                                                    }
                                                    else if(dataSnapshot.child(Constants.PAGO).getValue().toString().contains("1")){
                                                        cambiarEstadoPremium(1+"");
                                                    }
                                                }else{
                                                    dataSnapshot.child(Constants.PAGO).getRef().setValue(0);
                                                    cambiarEstadoPremium(0+"");
                                                }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else if (dataSnapshot.hasChild(Constants.FECHAPAGO)) {

                                    Long tiempoPago = Long.parseLong(dataSnapshot.child(Constants.FECHAPAGO).getValue().toString());
                                    dataSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int result = horaActual.compareTo(tiempoPago + Constants.UN_ANIO);
                                            if(dataSnapshot.hasChild(Constants.PAGO)){
                                                if (result > 0 || dataSnapshot.child(Constants.PAGO).getValue().toString().contains("0")) {
                                                    dataSnapshot.child(Constants.PAGO).getRef().setValue(0);
                                                    cambiarEstadoPremium(0 + "");
                                                } else if (dataSnapshot.child(Constants.PAGO).getValue().toString().contains("1")) {
                                                    cambiarEstadoPremium(1 + "");
                                                }
                                            }else{
                                                dataSnapshot.child(Constants.PAGO).getRef().setValue(0);
                                                cambiarEstadoPremium(0+"");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    response.getEntity().getContent().close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Error: " + e.getMessage());
            } catch (ParseException e) {
                Log.e(TAG, "doInBackground: Error" + e.getMessage());
            }
            return null;
        }

        // can use UI thread here
        protected void onPostExecute() {

        }
    }

}
