package com.vat.icare.chats.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.ColorRes;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vat.icare.R;
import com.vat.icare.calls.VideoCallActivity;
import com.vat.icare.doctor.DoctorChattingActivity;

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
    private Spannable getActionText(String title, @ColorRes int colorRes) {
        Spannable spannable = new SpannableString(title);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            spannable.setSpan(
                    new ForegroundColorSpan(this.getColor(colorRes)), 0, spannable.length(), 0);
        }
        return spannable;
    }

    private void showNotification(String msgBody, String name, String image) {

        if (msgBody.equals("Calling")) {
            int oneTimeID = (int) SystemClock.uptimeMillis();
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = "Incoming Call";
            Uri uri= Uri.parse("viauapp://");
            Uri notification_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            String notification_title= msgBody;

            Intent intent = new Intent(this, VideoCallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

            // notification action buttons start
            PendingIntent acptIntent = VideoCallActivity.getActionIntent(uri,this);
            PendingIntent rjctIntent = VideoCallActivity.getActionIntent(uri, this);

            NotificationCompat.Action rejectCall=new NotificationCompat.Action.Builder(R.drawable.rjt_btn,getActionText("Decline",android.R.color.holo_red_light),rjctIntent).build();
            NotificationCompat.Action acceptCall=new NotificationCompat.Action.Builder(R.drawable.acpt_btn,getActionText("Answer",android.R.color.holo_green_light),acptIntent).build();

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(notification_title)
                    .setContentText(name +"Calling")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setAutoCancel(true)
                    .setSound(notification_sound)
                    .addAction(acceptCall)
                    .addAction(rejectCall)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.mipmap.ic_launcher);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(
                        channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                mChannel.setSound(notification_sound,attributes);
                mChannel.setDescription(channelName);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                notificationManager.createNotificationChannel(mChannel);
            }
            notificationManager.notify(oneTimeID, notificationBuilder.build());
        }
        else {
            // Message Notification
            Intent intent = new Intent(this, DoctorChattingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            final PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_IMMUTABLE);

            final String channelId = getString(R.string.default_notification_channel_id);

            final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
            notificationBuilder
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(name + " Send Message")
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
    }

}
