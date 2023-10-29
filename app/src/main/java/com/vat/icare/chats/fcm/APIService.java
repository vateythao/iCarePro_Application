package com.vat.icare.chats.fcm;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({"Content-Type:application/json",
            "Authorization: key=AAAAOX38-uY:APA91bHWwbM4kijmASWmCwA54EdPsdoe1_lHvukAezblinGNMvY8UVy99LFxYVzbdmSQv8tSnCMmbibHvOrqVhhkdmvLCFYy_eySkxm2tSf_5lHD5Z18gVprrF1UHqCJivLpe5r29bSQ"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body Sender body);
}