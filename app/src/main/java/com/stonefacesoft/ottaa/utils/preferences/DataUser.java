package com.stonefacesoft.ottaa.utils.preferences;

import java.util.Calendar;

public class DataUser {

    private long birthDate = System.currentTimeMillis();
    private String gender ="";
    private String firstAndLastName="";
    private String email="";
    private String devices ="";


    public DataUser(){}

    public DataUser(String firstAndLastName, String gender, long birthDate, String email){
        this.gender=gender;
        this.birthDate = birthDate;
        this.firstAndLastName=firstAndLastName;
        this.email=email;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstAndLastName() {
        return firstAndLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public void setFirstAndLastName(String firstAndLastName) {
        this.firstAndLastName = firstAndLastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDevices(String devices){
        this.devices =devices;
    }

    public String getDevices() {
        return devices;
    }

    public int getUserAge(){
        Calendar actualDay=Calendar.getInstance();
        Calendar birthDayDate=Calendar.getInstance();
        birthDayDate.setTimeInMillis(birthDate);
        int age=actualDay.get(Calendar.YEAR)-birthDayDate.get(Calendar.YEAR);
        return age;
    }



}
