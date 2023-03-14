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

    @Test
    public void testGetPlaceNameRestaurant() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_restaurant)).thenReturn("Restaurant");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("RESTAURANT");
        assertNotNull(placeName);
    }
    @Test
    public void testGetPlaceNameBakery() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_bakery)).thenReturn("Bakery");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BAKERY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNamePark() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_park)).thenReturn("Park");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PARK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameStore() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_store)).thenReturn("Store");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameShoppingMall() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_shopping_mall)).thenReturn("Shopping Mall");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("SHOPPING_MALL");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameAirport() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_airport)).thenReturn("AirPort");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("AIRPORT");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNameATM() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_atm)).thenReturn("ATM");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("ATM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBank() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_bank)).thenReturn("Bank");

        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BANK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBar() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_bar)).thenReturn("Bar");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BAR");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceInsuranceAgency() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_insurance_agency)).thenReturn("Insurance Agency");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("INSURANCE_AGENCY");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceSubwayStation() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_subway_station)).thenReturn("Subway Station");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("SUBWAY_STATION");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlaceJewerlyStore() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_jewerle_store)).thenReturn("Jewerly Store");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("JEWELRY_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceBusStation() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_bus_station)).thenReturn("Bus Station");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("BUS_STATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTaxiStand() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_taxi_stand)).thenReturn("Taxi Stand");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("TAXI_STAND");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceStadium() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_stadium)).thenReturn("Stadium");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("STADIUM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceHospital() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_hospital)).thenReturn("Hospital");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("HOSPITAL");
        assertNotNull(placeName);
    }

    @Test
    public void testGetMealDelivery() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_meal_delivery)).thenReturn("Meal Delivery");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MEAL_DELIVERY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceMuseum() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_museum)).thenReturn("Museum");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MUSEUM");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceHairCare() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constat_hair_care)).thenReturn("Hair Care");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("HAIR_CARE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTrainStation() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_train_station)).thenReturn("Train Station");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("TRAIN_STATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceMovieTheater() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_movie_theater)).thenReturn("Movie Theater");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("MOVIE_THEATER");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceCafe() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_cafe)).thenReturn("Cafe");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CAFE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceSchool() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_school)).thenReturn("School");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("SCHOOL");
        assertNotNull(placeName);
    }



    @Test
    public void testGetPlaceLodging() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_lodginf)).thenReturn("Lodging");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LODGING");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceLaundry() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_laudry)).thenReturn("Laundry");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LAUNDRY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlacePetStore() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_petstore)).thenReturn("Pet Store");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PET_STORE");
        assertNotNull(placeName);
    }


    @Test
    public void testGetPlacePharmacy() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_pharmacy)).thenReturn("Pharmacy");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PHARMACY");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceZoo() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);

        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_zoo)).thenReturn("Zoo");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("ZOO");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceDentist() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_dentist)).thenReturn("Dentist");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("DENTIST");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceNightClub() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_nigth_club)).thenReturn("Night Club");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("NIGHT_CLUB");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlacePhysiotherapist() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_physiotherapist)).thenReturn("Phisiotherapist");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("PHYSIOTHERAPIST");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceAmusementPark() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_park)).thenReturn("Amusement Park");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("AMUSEMENT_PARK");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceClothingStore() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_clothing_store)).thenReturn("Clothing Store");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CLOTHING_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceChurch() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_church)).thenReturn("Church");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CHURCH");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceConvenienceStore() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.str_constant_convenience_store)).thenReturn("Convenience Store");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("CONVENIENCE_STORE");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceLocation() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        // Set up the behavior of the mock resources object
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(R.string.location)).thenReturn("Location");
        // Call the method being tested
        String placeName = placesImplementation.getPlaceName("LOCATION");
        assertNotNull(placeName);
    }

    @Test
    public void testGetPlaceTest() {
        PlacesImplementation placesImplementation = new PlacesImplementation(context);
        assertFalse(placesImplementation.isStarted());
    }





}
