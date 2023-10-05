package com.vat.icare.pojo;

public class Doctor {
    private String token, name, speciality, gender,medicalDegree ,image;
    private long date;

    public Doctor() {

    }

    public Doctor(String token, String name, String speciality, String gender, String medicalDegree, String image, long date) {
        this.token = token;
        this.name = name;
        this.speciality = speciality;
        this.gender = gender;
        this.medicalDegree = medicalDegree;
        this.image = image;
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalDegree() {
        return medicalDegree;
    }

    public void setMedicalDegree(String medicalDegree) {
        this.medicalDegree = medicalDegree;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
