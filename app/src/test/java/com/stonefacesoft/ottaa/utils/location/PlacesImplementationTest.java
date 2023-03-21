package com.stonefacesoft.ottaa.utils.location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.stonefacesoft.ottaa.Prediction.Posicion;
import com.stonefacesoft.ottaa.R;

import junit.framework.AssertionFailedError;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.res.Resources;


public class PlacesImplementationTest {


    private Context context = Mockito.mock(Context.class);
    Resources resources = Mockito.mock(Resources.class);
    private PlacesImplementation placesImplementation;

    @Before
    public void setUp() {
        // Initialize resources or dependencies needed for tests
        placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_restaurant)).thenReturn("Restaurant");
        Mockito.when(resources.getString(R.string.str_constant_bakery)).thenReturn("Bakery");
        Mockito.when(resources.getString(R.string.str_constant_park)).thenReturn("Park");
        Mockito.when(resources.getString(R.string.str_constant_store)).thenReturn("Store");
        Mockito.when(resources.getString(R.string.str_constant_shopping_mall)).thenReturn("Shopping Mall");
        Mockito.when(resources.getString(R.string.str_constant_airport)).thenReturn("AirPort");
        Mockito.when(resources.getString(R.string.str_constant_atm)).thenReturn("ATM");
        Mockito.when(resources.getString(R.string.str_constant_bank)).thenReturn("Bank");
        Mockito.when(resources.getString(R.string.str_constant_bar)).thenReturn("Bar");
        Mockito.when(resources.getString(R.string.str_constant_insurance_agency)).thenReturn("Insurance Agency");
        Mockito.when(resources.getString(R.string.str_constant_subway_station)).thenReturn("Subway Station");
        Mockito.when(resources.getString(R.string.str_constant_jewerle_store)).thenReturn("Jewerly Store");
        Mockito.when(resources.getString(R.string.str_constant_bus_station)).thenReturn("Bus Station");
        Mockito.when(resources.getString(R.string.str_constant_taxi_stand)).thenReturn("Taxi Stand");
        Mockito.when(resources.getString(R.string.str_constant_stadium)).thenReturn("Stadium");
        Mockito.when(resources.getString(R.string.str_constant_hospital)).thenReturn("Hospital");
        Mockito.when(resources.getString(R.string.str_constant_meal_delivery)).thenReturn("Meal Delivery");
        Mockito.when(resources.getString(R.string.str_constant_museum)).thenReturn("Museum");
        Mockito.when(resources.getString(R.string.str_constat_hair_care)).thenReturn("Hair Care");
        Mockito.when(resources.getString(R.string.str_constant_train_station)).thenReturn("Train Station");
        Mockito.when(resources.getString(R.string.str_constant_movie_theater)).thenReturn("Movie Theater");
        Mockito.when(resources.getString(R.string.str_constant_cafe)).thenReturn("Cafe");
        Mockito.when(resources.getString(R.string.str_constant_school)).thenReturn("School");
        Mockito.when(resources.getString(R.string.str_constant_lodginf)).thenReturn("Lodging");
        Mockito.when(resources.getString(R.string.str_constant_laudry)).thenReturn("Laundry");
        Mockito.when(resources.getString(R.string.str_constant_petstore)).thenReturn("Pet Store");
        Mockito.when(resources.getString(R.string.str_constant_pharmacy)).thenReturn("Pharmacy");
        Mockito.when(resources.getString(R.string.str_constant_zoo)).thenReturn("Zoo");
        Mockito.when(resources.getString(R.string.str_constant_dentist)).thenReturn("Dentist");
        Mockito.when(resources.getString(R.string.str_constant_nigth_club)).thenReturn("Night Club");
        Mockito.when(resources.getString(R.string.str_constant_physiotherapist)).thenReturn("Phisiotherapist");
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_park)).thenReturn("Amusement Park");
        Mockito.when(resources.getString(R.string.str_constant_clothing_store)).thenReturn("Clothing Store");
        Mockito.when(resources.getString(R.string.str_constant_church)).thenReturn("Church");
        Mockito.when(resources.getString(R.string.str_constant_convenience_store)).thenReturn("Convenience Store");
        Mockito.when(resources.getString(R.string.location)).thenReturn("Location");
    }

    @Test
    public void testGetPlaceNameRestaurant() {


        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("RESTAURANT");
        assertNotNull(placeName);
    }
    @Test
    public void testGetPlaceNameBakery() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BAKERY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNamePark() {

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PARK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameStore() {

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameShoppingMall() {
        String placeName = placesImplementation.getPlaceName("SHOPPING_MALL");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameAirport() {
        String placeName = placesImplementation.getPlaceName("AIRPORT");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameATM() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("ATM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBank() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BANK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBar() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BAR");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceInsuranceAgency() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("INSURANCE_AGENCY");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceSubwayStation() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("SUBWAY_STATION");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceJewerlyStore() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("JEWELRY_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBusStation() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BUS_STATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTaxiStand() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("TAXI_STAND");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceStadium() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("STADIUM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceHospital() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("HOSPITAL");
        assertNotNull(placeName);
    }

    @Test
    public void testGetMealDelivery() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MEAL_DELIVERY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceMuseum() {
       

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MUSEUM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceHairCare() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("HAIR_CARE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTrainStation() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("TRAIN_STATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceMovieTheater() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MOVIE_THEATER");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceCafe() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CAFE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceSchool() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("SCHOOL");
        assertNotNull(placeName);
    }



    @Test
    public void testGetPlaceLodging() {
       

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LODGING");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceLaundry() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LAUNDRY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlacePetStore() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PET_STORE");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlacePharmacy() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PHARMACY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceZoo() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("ZOO");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceDentist() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("DENTIST");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNightClub() {
       
        // Set up the behavior of the mock resources object
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("NIGHT_CLUB");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlacePhysiotherapist() {
       
        // Set up the behavior of the mock resources object

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PHYSIOTHERAPIST");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceAmusementPark() {

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("AMUSEMENT_PARK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceClothingStore() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CLOTHING_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceChurch() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CHURCH");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceConvenienceStore() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CONVENIENCE_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceLocation() {
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LOCATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTest() {
       
        assertFalse(placesImplementation.isStarted());
    }





}
