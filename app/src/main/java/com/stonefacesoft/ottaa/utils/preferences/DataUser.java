package com.stonefacesoft.ottaa.utils.preferences;

public class DataUser {

    private long birthDate;
    private String gender;
    private String firstAndLastName;
    private String email;

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
}
