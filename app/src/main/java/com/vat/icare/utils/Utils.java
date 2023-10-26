package com.vat.icare.utils;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class Utils {

    public static String REF_CHATS = "Chats_v2";
    public static String SLASH = File.separator;

    public static String getChatUniqueId() {
        return FirebaseDatabase.getInstance().getReference().child(REF_CHATS).child("").push().getKey();
    }

}
