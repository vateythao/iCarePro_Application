package com.vat.icare.chats.fcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * @author : Prashant Adesara
 * @url https://www.bytesbee.com
 * Manage the data into local storage via SharedPreference
 */
public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "BytesBeeChatV1";
    private static final String KEY_ON_OFF_NOTIFICATION = "onOffNotification";
    private static final String KEY_ON_OFF_RTL = "onOffRTL";
    private static final String KEY_ONBOARDING = "isOnBoardingDone";
    private static final String KEY_IS_LOGIN = "isLoginDone";
    private static final String LOGIN_USER_TYPE = "loginUserType";
    private final SharedPreferences pref;


    boolean TRUE = true;
    boolean FALSE = false;

    //============== START

    private static SessionManager mInstance;

    public static SessionManager get() {
        return mInstance;
    }

    public static void init(Context ctx) {
        if (mInstance == null) mInstance = new SessionManager(ctx);
    }

    //============== END

    public SessionManager(final Context context) {
        pref = context.getSharedPreferences(context.getPackageName() + PREF_NAME, 0);
    }

    public void setOnOffNotification(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ON_OFF_NOTIFICATION, value);
        editor.apply();
    }

    public boolean isNotificationOn() {
        return pref.getBoolean(KEY_ON_OFF_NOTIFICATION, TRUE);
    }

    public void setOnOffRTL(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ON_OFF_RTL, value);
        editor.apply();
    }

    public boolean isRTLOn() {
        return pref.getBoolean(KEY_ON_OFF_RTL, FALSE);
    }

    public void setOnBoardingDone(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ONBOARDING, value);
        editor.apply();
    }

    public void setIsLoginDone(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGIN, value);
        editor.apply();
    }

    public void setLoginUserType(final String value) {
        final Editor editor = pref.edit();
        editor.putString(LOGIN_USER_TYPE, value);
        editor.apply();
    }

    public boolean isOnBoardingDone() {
        return pref.getBoolean(KEY_ONBOARDING, FALSE);
    }

    public boolean isLoginDone(){ return pref.getBoolean(KEY_IS_LOGIN, FALSE); }
    public String loginUserType(){ return pref.getString(LOGIN_USER_TYPE, ""); }

    public void clearAll() {
        final Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
