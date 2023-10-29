package com.vat.icare.chats.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vat.icare.R;
import com.vat.icare.doctor.DoctorChattingActivity;
import com.vat.icare.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> mapData = remoteMessage.getData();
        Log.v(TAG, remoteMessage.getData().toString());

        String firebase1 = remoteMessage.getData().get("sent");
        String firebase2 = remoteMessage.getData().get("user");

        // Firebase Data
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(firebase2);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name=dataSnapshot.child("name").getValue(String.class);
                    String image=dataSnapshot.child("image").getValue(String.class);
                    showNotification(mapData.get("body"),name,image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error here
            }
        });
    }

    private void showNotification(String msgBody, String name, String image) {
        Intent intent;

        intent = new Intent(this, DoctorChattingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        final String channelId = getString(R.string.default_notification_channel_id);

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(name+" Send Message")
                .setContentText(msgBody)
                .setTicker(msgBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId,
                    "Chat Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
    }

    private String strGroups = "";
    private String type = "";
    private String username = "";

    /*private void sendNotification(RemoteMessage remoteMessage) {
        final String user = remoteMessage.getData().get(FCM_USER);
        final String icon = remoteMessage.getData().get(FCM_ICON);
        final String title = remoteMessage.getData().get(FCM_TITLE);
        final String message = remoteMessage.getData().get(FCM_BODY);
        try {
            type = remoteMessage.getData().get(FCM_TYPE);
            username = remoteMessage.getData().get(FCM_USERNAME);
        } catch (Exception ignored) {
        }
        try {
            strGroups = remoteMessage.getData().get(FCM_GROUPS);
        } catch (Exception ignored) {
        }

        Bitmap bitmap = null;

        String body;
        if (!Utils.isEmpty(type)) {
            if (type.equalsIgnoreCase(TYPE_IMAGE)) {
                bitmap = getBitmapFromURL(message);
                body = String.format(getString(R.string.strPhotoSent), username);
            } else {
                body = username + ": " + message;
            }
        } else {
            body = message;
        }

        final Bundle bundle = new Bundle();

        Intent intent;
        if (Utils.isEmpty(strGroups)) {
            intent = new Intent(this, MessageActivity.class);
            bundle.putString(EXTRA_USER_ID, user);
        } else {
            intent = new Intent(this, GroupsMessagesActivity.class);
            Groups groups = new Gson().fromJson(strGroups, Groups.class);
            bundle.putSerializable(EXTRA_OBJ_GROUP, groups);
        }

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        final String channelId = getString(R.string.default_notification_channel_id);

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(bitmap));
        }

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, "Chat Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
    }*/

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            final URL url = new URL(strURL);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            //Utils.getErrors(e);
            return null;
        }
    }

}
