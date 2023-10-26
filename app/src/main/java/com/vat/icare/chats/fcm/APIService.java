package com.vat.icare.chats.fcm;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAApn-MikU:APA91bFGriSaIuSIvWy0Ws8CfyeuHeR9b_MYiQ0Q2oLTQdRxDCqtm_PyRbysnyfJqxZQw7JO_gr7i6bz_tUtPYuN4h8l1JAGEUDX81XHGggXczHMrz6QNg_hwgFNkWJh9smj0XvgSRhI"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
