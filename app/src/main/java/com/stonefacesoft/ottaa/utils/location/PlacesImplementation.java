package com.stonefacesoft.ottaa.utils.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.R;
import com.stonefacesoft.ottaa.utils.ConstantsPlaces;
import com.stonefacesoft.ottaa.utils.TraducirFrase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacesImplementation {
    private final Context mContext;
    private PlacesClient client;
    private final String TAG = "PlacesImplementation";
    private final LocationManager locationManager;
    private ArrayList<Place> places;
    private int positionPlace = -1;
    private int positionType = -1;
    private boolean isStarted;
    private String nombreTraducido;


    public PlacesImplementation(Context mContext) {
        this.mContext = mContext;
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void iniciarClientePlaces() {

        Places.initialize(mContext, mContext.getString(R.string.places_api_key));
        if (Places.isInitialized()) {
            client = Places.createClient(mContext);
            isStarted = true;
        }
    }

    public PlacesClient getClient() {
        if (client != null)
            return client;
        else
            iniciarClientePlaces();
        return client;
    }

    /*
     * If you want to find the location where you are with places
     * you can use this function
     * that's function request
     * */
    public void locationRequest() {
        positionType = -1;
        positionPlace = -1;
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.TYPES, Place.Field.ADDRESS);
        places = new ArrayList<>();

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else{
            Task<FindCurrentPlaceResponse> placeResponse = client.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FindCurrentPlaceResponse response=task.getResult();
                    //use that's for each to load the searches places
                    for (PlaceLikelihood placeLikelihood:response.getPlaceLikelihoods()){

                        //print in the console the address for the place
                        Log.d(TAG, "locationRequest: "+ placeLikelihood.getLikelihood()+ " name :"+placeLikelihood.getPlace().getName());
                        // add the place to the list
                        if(placeLikelihood.getLikelihood()>=0.09&&placeLikelihood.getLikelihood()>0.50)
                            places.add(placeLikelihood.getPlace());

                    }
                }else{
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }

            } ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Failure in location request");
                }
            });
        }


    }



    public int typesSizes(){
        return places.size();
    }
    /*
    * that's metods return the Place.Type of
    *
    * */
    public Place getPlace(){
        if(positionType==-1)
        positionPlace++;
        if(positionPlace>=typesSizes())
            positionPlace=0;
        return places.get(positionPlace);
    }

    public int getPositionType(){
        return positionPlace;
    }

    public String getPlaceName(Place place){
        if(place!=null)
           return place.getName();
            return "";
    }
    public String getPlaceType(Place place){
        positionType++;
        if(place.getTypes().size()>0&&positionType<place.getTypes().size())
            return place.getTypes().get(positionType).name();
        else
            positionType=-1;
        return "";
    }



    private String getPlaceTypeFromValue(int value) {
        Field[] fields = ConstantsPlaces.class.getDeclaredFields();
        String name;
        for (Field field : fields) {

            name = field.getName().toLowerCase();
            try {
                if (field.getInt(name) == value) {
                    return name.replace("type_", "");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getPlaceName(String name){

            switch (name) {
               case "RESTAURANT":
                    return mContext.getResources().getString(R.string.str_constant_restaurant);
                case "BAKERY":
                    return mContext.getResources().getString(R.string.str_constant_bakery);
                case "PARK":
                    return mContext.getResources().getString(R.string.str_constant_park);
                case "STORE":
                    return mContext.getResources().getString(R.string.str_constant_store);
                case "SHOPPING_MALL":
                    return mContext.getResources().getString(R.string.str_constant_shopping_mall);
                case "AIRPORT":
                    return mContext.getResources().getString(R.string.str_constant_airport);
                case "ATM":
                    return mContext.getResources().getString(R.string.str_constant_atm);
                case "BANK":
                    return mContext.getResources().getString(R.string.str_constant_bank);
                case "BAR":
                    return mContext.getResources().getString(R.string.str_constant_bar);
                case "INSURANCE_AGENCY":
                    return mContext.getResources().getString(R.string.str_constant_insurance_agency);
                case "SUBWAY_STATION":
                    return mContext.getResources().getString(R.string.str_constant_subway_station);
                case "JEWELRY_STORE":
                    return mContext.getResources().getString(R.string.str_constant_jewerle_store);
                case "BUS_STATION":
                    return mContext.getResources().getString(R.string.str_constant_bus_station);
                case "TAXI_STAND":
                    return mContext.getResources().getString(R.string.str_constant_taxi_stand);
                case "STADIUM":
                    return mContext.getResources().getString(R.string.str_constant_stadium);
                case "HOSPITAL":
                    return mContext.getResources().getString(R.string.str_constant_hospital);
                case "MEAL_DELIVERY":
                    return mContext.getResources().getString(R.string.str_constant_meal_delivery);
                case "MUSEUM":
                    return mContext.getResources().getString(R.string.str_constant_museum);
                case "HAIR_CARE":
                    return mContext.getResources().getString(R.string.str_constat_hair_care);
                case "TRAIN_STATION":
                    return mContext.getResources().getString(R.string.str_constant_train_station);
                case "MOVIE_THEATER":
                    return mContext.getResources().getString(R.string.str_constant_movie_theater);
                case "CAFE":
                    return mContext.getResources().getString(R.string.str_constant_cafe);
                case "SCHOOL":
                    return mContext.getResources().getString(R.string.str_constant_school);
                case "ZOO":
                    return mContext.getResources().getString(R.string.str_constant_zoo);
                case "LAUNDRY":
                    return mContext.getResources().getString(R.string.str_constant_laudry);
                case "LODGING":
                    return mContext.getResources().getString(R.string.str_constant_lodginf);
                case "PET_STORE":
                    return mContext.getResources().getString(R.string.str_constant_petstore);
                case "PHARMACY":
                    return mContext.getResources().getString(R.string.str_constant_pharmacy);
                case "DENTIST":
                    return mContext.getResources().getString(R.string.str_constant_dentist);
                case "NIGHT_CLUB":
                    return mContext.getResources().getString(R.string.str_constant_nigth_club);
                case "PHYSIOTHERAPIST":
                    return mContext.getResources().getString(R.string.str_constant_physiotherapist);
                case "AMUSEMENT_PARK":
                    return mContext.getResources().getString(R.string.str_constant_park);
                case "CLOTHING_STORE":
                    return mContext.getResources().getString(R.string.str_constant_clothing_store);
                case "CHURCH":
                    return mContext.getResources().getString(R.string.str_constant_church);
                case "CONVENIENCE_STORE":
                    return mContext.getResources().getString(R.string.str_constant_convenience_store);
                default:

                    return "";

            }

    }





    public boolean isStarted() {
        return isStarted;
    }
}
