package com.vat.icare.calls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.vat.icare.R;
import com.vat.icare.chats.media.RtcTokenBuilder2;
import com.vat.icare.databinding.ActivityAudioCallBinding;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;

public class AudioCallActivity extends AppCompatActivity {

    // Fill the App ID of your project generated on Agora Console.
    private final String appId = "caae4abb903349e890872ea2edf59feb";
    // Fill the channel name.
    private String channelName = "iCarePro";
    // Fill the temp token generated on Agora Console.
    private String token = null;
    private String appCertificate = "55a68d6c33ff4fdfab540f05e53a1cd0";
    // An integer that identifies the local user.
    private int uid = 0;
    // Track the status of your connection
    private boolean isJoined = false;

    // Agora engine instance
    private RtcEngine agoraEngine;

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS =
            {
                    Manifest.permission.RECORD_AUDIO
            };

    private boolean checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    void showMessage(String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

    private void setupVoiceSDKEngine() {
        try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote user joining the channel.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(() -> binding.infoText.setText("Remote user joined: " + uid));
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            // Successfully joined a channel
            isJoined = true;
            showMessage("Joined Channel " + channel);
            runOnUiThread(() -> binding.infoText.setText("Waiting for a remote user to join"));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            // Listen for remote users leaving the channel
            showMessage("Remote user offline " + uid + " " + reason);
            if (isJoined)
                runOnUiThread(() -> binding.infoText.setText("Waiting for a remote user to join"));
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            // Listen for the local user leaving the channel
            runOnUiThread(() -> binding.infoText.setText("Press the button to join a channel"));
            isJoined = false;
        }
    };

    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.autoSubscribeAudio = true;
        // Set both clients as the BROADCASTER.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        // Set the channel profile as BROADCASTING.
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
        agoraEngine.joinChannel(token, channelName, uid, options);
    }

    public void joinLeaveChannel() {
        if (isJoined) {
            agoraEngine.leaveChannel();
        } else {
            joinChannel();
        }
    }

    ActivityAudioCallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio_call);

        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();
        int timeStamp = (int) (System.currentTimeMillis() / 1000 + 300);
        token = tokenBuilder.buildTokenWithUid(
                appId, appCertificate, channelName, uid, RtcTokenBuilder2.Role.ROLE_PUBLISHER, timeStamp, timeStamp);

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        setupVoiceSDKEngine();

        // Set up access to the UI element
        binding.btnJoinCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinChannel();
            }
        });

        binding.LeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinLeaveChannel();
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        agoraEngine.leaveChannel();

        // Destroy the engine in a sub-thread to avoid congestion
        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        }).start();
    }
}