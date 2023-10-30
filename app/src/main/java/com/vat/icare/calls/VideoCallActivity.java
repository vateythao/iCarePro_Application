package com.vat.icare.calls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.vat.icare.R;
import com.vat.icare.databinding.ActivityVideoCallBinding;
import com.vat.icare.main.MainActivity;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoCallActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
    ActivityVideoCallBinding binding;
    Context context;
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;
    private static String API_KEY = "47800761";
    private static String SESSION_ID = "2_MX40NzgwMDc2MX5-MTY5ODY4NDAzMjg0OH5Db3Evd29XVDExRzREa0U5UVQ0NlJ5aW5-fn4";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NzgwMDc2MSZzaWc9OTIyOGFlYzcxNDNjZTQ5ODg2NTcyNDZkYTQwODA0YjU3ZDVjZTJlMTpzZXNzaW9uX2lkPTJfTVg0ME56Z3dNRGMyTVg1LU1UWTVPRFk0TkRBek1qZzBPSDVEYjNFdmQyOVhWREV4UnpSRWEwVTVVVlEwTmxKNWFXNS1mbjQmY3JlYXRlX3RpbWU9MTY5ODY4NDA1MiZub25jZT0wLjkzMTA4NDc3MjA5MDczNDMmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTcwMTI3NjA1MSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";

    private Session mSession;
    private Publisher mPublisher;
    private Subscriber mSubscriber;
    private ImageView cancel_call_btn;
    private FrameLayout mPublisherView;
    private FrameLayout mSubscriberView;

    private DatabaseReference userref;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_call);
        context=this;
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("name");

        binding.imgCutcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(userID).hasChild("Incoming")) {
                            userref.child(userID).child("Incoming").removeValue();

                            if (mPublisher != null) {
                                mPublisher.destroy();
                            }
                            if (mSubscriber != null) {
                                mSubscriber.destroy();
                            }
                        }

                        if (snapshot.child(userID).hasChild("Outgoing")) {
                            userref.child(userID).child("Outgoing").removeValue();

                            if (mPublisher != null) {
                                mPublisher.destroy();
                            }
                            if (mSubscriber != null) {
                                mSubscriber.destroy();
                            }
                        }
                        startActivity(new Intent(VideoCallActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoCallActivity.this);

    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermission() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this, perms)) {
            //Initialize The Stream
            mPublisherView = findViewById(R.id.jisne_call_kiya_hai_container);
            mSubscriberView = findViewById(R.id.jisko_call_kiya_hai_container);

            mSession = new com.opentok.android.Session.Builder(this, API_KEY, SESSION_ID).build();
            mSession.setSessionListener(VideoCallActivity.this);
            mSession.connect(TOKEN);


        } else {
            EasyPermissions.requestPermissions(this, "MIC and Camera Permissions Required to Access the App", RC_VIDEO_APP_PERM, perms);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(com.opentok.android.Session session) {
        //Publish The Stream to the Session
        Log.d(LOG_TAG, "Session Connected");
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(VideoCallActivity.this);

        mPublisherView.addView(mPublisher.getView());

        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }

        mSession.publish(mPublisher);

    }

    @Override
    public void onDisconnected(com.opentok.android.Session session) {

    }

    @Override
    public void onStreamReceived(com.opentok.android.Session session, Stream stream) {
        //Receiver Receiving the Stream
        Log.d(LOG_TAG, "Session Received");

        if (mSubscriber == null) {
            mSubscriber = new com.opentok.android.Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);
            mSubscriberView.addView(mSubscriber.getView());
            cancel_call_btn.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onStreamDropped(com.opentok.android.Session session, Stream stream) {
        Log.d(LOG_TAG, "Session Dropped");

        if (mSubscriber != null) {
            mSubscriber = null;
            mSubscriberView.removeAllViews();
        }

    }

    @Override
    public void onError(com.opentok.android.Session session, OpentokError opentokError) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}