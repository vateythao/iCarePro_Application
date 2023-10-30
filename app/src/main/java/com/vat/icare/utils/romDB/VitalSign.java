package com.vat.icare.utils.romDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vitalSigns_table")
public class VitalSign {
    @PrimaryKey(autoGenerate = true)
    int id;
    @NonNull
    String name;
    int bloodP_sys;
    int bloodP_dia;
    int heart_rate;
    int sugar;
    int pulse;
    String date;

    public VitalSign(@NonNull String name, int bloodP_sys, int bloodP_dia, int heart_rate, int sugar, int pulse, String date) {
        this.name = name;
        this.bloodP_sys = bloodP_sys;
        this.bloodP_dia = bloodP_dia;
        this.heart_rate = heart_rate;
        this.sugar = sugar;
        this.pulse = pulse;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getBloodP_sys() {
        return bloodP_sys;
    }

    public void setBloodP_sys(int bloodP_sys) {
        this.bloodP_sys = bloodP_sys;
    }

    public int getBloodP_dia() {
        return bloodP_dia;
    }

    public void setBloodP_dia(int bloodP_dia) {
        this.bloodP_dia = bloodP_dia;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
}
