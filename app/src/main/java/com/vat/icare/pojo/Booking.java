package com.vat.icare.pojo;

public class Booking {
    String date;
    String nameDoctor;
    String patientName;
    String firebaseId;
    String doctorId;
    String patientId;
    String time;

    public Booking() {
    }

    public Booking(String date, String nameDoctor, String patientName, String firebaseId, String doctorId, String patientId, String time) {
        this.date = date;
        this.nameDoctor = nameDoctor;
        this.patientName = patientName;
        this.firebaseId = firebaseId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.time = time;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
