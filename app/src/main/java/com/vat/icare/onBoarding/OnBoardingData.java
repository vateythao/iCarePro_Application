package com.vat.icare.onBoarding;

/**
 * Created by Dell on 24,July,2023
 */
public class OnBoardingData {
    String title;
    String decs;
    int image;

    public OnBoardingData(String title, String decs, int image) {
        this.title = title;
        this.decs = decs;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
