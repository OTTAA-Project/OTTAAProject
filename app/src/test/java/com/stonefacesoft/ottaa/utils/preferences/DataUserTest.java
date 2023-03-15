package com.stonefacesoft.ottaa.utils.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DataUserTest {

    public DataUser dataUser;

    @Before
    public void setUp() throws Exception {
        dataUser = new DataUser("Paul","Male",1000,"fake@mail.com");
    }

    @Test
    public void getBirthDate() {
        long date = System.currentTimeMillis();
        dataUser.setBirthDate(date);
        assertEquals(date,dataUser.getBirthDate());
    }

    @Test
    public void getGender() {
        dataUser.setGender("Male");
        assertEquals("Male",dataUser.getGender());
    }

    @Test
    public void getGenderEmpty() {
        dataUser.setGender("");
        assertEquals("",dataUser.getGender());
    }

    @Test
    public void getFirstAndLastName() {
        dataUser.setFirstAndLastName("Paul");
        assertEquals("Paul",dataUser.getFirstAndLastName());
    }

    @Test
    public void getFirstAndLastNameEmptyValue(){
        dataUser.setFirstAndLastName("");
        assertEquals("",dataUser.getFirstAndLastName());
    }

    @Test
    public void getEmail() {
        dataUser.setEmail("fake@gmail.com");
        assertEquals("fake@gmail.com",dataUser.getEmail());
    }

    @Test
    public void getEmailEmptyValue(){
        dataUser.setEmail("");
        assertEquals("",dataUser.getEmail());
    }


    @Test
    public void setBirthDate() {
        long date = System.currentTimeMillis();
        dataUser.setBirthDate(date);
        assertEquals(date,dataUser.getBirthDate());
    }

    @Test
    public void setFirstAndLastName() {
        dataUser.setFirstAndLastName("Paul");
        assertNotNull(dataUser.getFirstAndLastName());
    }

    @Test
    public void setGender() {
        dataUser.setGender("Male");
        assertNotNull(dataUser.getGender());
    }


    @Test
    public void setEmail() {
        dataUser.setEmail("yo@gmail.com");
        assertNotNull(dataUser.getEmail());
    }

    @Test
    public void getUserAge() {
        dataUser.setBirthDate(System.currentTimeMillis());
        assertEquals(0,dataUser.getUserAge());
    }

    @Test
    public void  setDevices(){
        dataUser.setDevices("Huawei");
        assertEquals("Huawei",dataUser.getDevices());
    }
    @Test
    public void  getDevicesEmpty(){
        assertEquals("",dataUser.getDevices());
    }

    @Test
    public void  getDevices(){
        dataUser.setDevices("Samsung");
        assertEquals("Samsung",dataUser.getDevices());
    }


}