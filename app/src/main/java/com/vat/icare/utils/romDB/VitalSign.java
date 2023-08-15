package com.vat.icare.utils.romDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vitalSigns")
public class VitalSign {
    @PrimaryKey
    @NonNull
    String name;
    String bloodP_pressure;
    String heart_rate;
    String sugar;
    String pulse;

    public VitalSign(@NonNull String name, String bloodP_pressure, String heart_rate, String sugar, String pulse) {
        this.name = name;
        this.bloodP_pressure = bloodP_pressure;
        this.heart_rate = heart_rate;
        this.sugar = sugar;
        this.pulse = pulse;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getBloodP_pressure() {
        return bloodP_pressure;
    }

    public void setBloodP_pressure(String bloodP_pressure) {
        this.bloodP_pressure = bloodP_pressure;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }
}
