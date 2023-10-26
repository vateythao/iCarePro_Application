package com.vat.icare.chats.fcm;

import com.vat.icare.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(final String url) {
        if (retrofit == null) {

            HttpLoggingInterceptor mHttpLoginInterceptor = new HttpLoggingInterceptor();

            mHttpLoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder mOkClient = new OkHttpClient.Builder().readTimeout(300,
                    TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS).connectTimeout(300, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                mOkClient.addInterceptor(mHttpLoginInterceptor);
                //mOkClient.addInterceptor(new ChuckInterceptor(App.getInstance()));
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(mOkClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
