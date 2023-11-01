package com.vat.icare.doctor;

import static com.vat.icare.utils.Utils.REF_CHATS;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.vat.icare.R;
import com.vat.icare.calls.VideoCallActivity;
import com.vat.icare.chats.MessageAdapter;
import com.vat.icare.chats.fcm.APIService;
import com.vat.icare.chats.fcm.Data;
import com.vat.icare.chats.fcm.RetroClient;
import com.vat.icare.chats.fcm.Sender;
import com.vat.icare.chats.fcm.SessionManager;
import com.vat.icare.databinding.ActivityDoctorChattingBinding;
import com.vat.icare.pojo.Doctor;
import com.vat.icare.pojo.Message;
import com.vat.icare.pojo.User;
import com.vat.icare.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorChattingActivity extends AppCompatActivity {

    private final String TAG = "CA/ChatActivity";

    // Will handle all changes happening in database

    private DatabaseReference userDatabase, chatDatabase;
    private ValueEventListener userListener, chatListener;

    // Will handle old/new messages between users

    private Query messagesDatabase;
    private ChildEventListener messagesListener;

    private MessageAdapter messagesAdapter;
    private final List<Message> messagesList = new ArrayList<>();

    // User data
    private APIService apiService;

    private String currentUserId;

    // activity_chat views

    private EditText messageEditText;
    private ImageView chat_send;
    TextView txtv_chatUserName;
    ImageView backPressChat;
    String FCM_URL = "https://fcm.googleapis.com/";
    SessionManager sessionManager;

    // Will be used on Notifications to detairminate if user has chat window open

    public static String otherUserId, otherUserToken;
    public static boolean running = false;
    ActivityDoctorChattingBinding binding;
    private String strSender, strReceiver;

     Doctor doctor;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_chatting);

        messageEditText = findViewById(R.id.input);
        chat_send = findViewById(R.id.chat_send);

        apiService = RetroClient.getClient(FCM_URL).create(APIService.class);

        otherUserId = getIntent().getStringExtra("useridFirebase");
        otherUserToken = getIntent().getStringExtra("userTokenFirebase");
        String userName = getIntent().getStringExtra("userName");
        String proficpic = getIntent().getStringExtra("proficpic");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.imgVideoCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("Calling");
                Intent intent = new Intent(DoctorChattingActivity.this, VideoCallActivity.class);
                intent.putExtra("visiter_id", otherUserId);
                startActivity(intent);
            }
        });

        if(proficpic!=null){
            byte[] imageAsBytes = Base64.decode(proficpic.getBytes(), Base64.DEFAULT);
            binding.imgChatOther.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        binding.imgBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);

        Log.v("FirebaseInitDatabaseValue", "OtherUserId: " + otherUserId + "CurrentUserID: " + currentUserId);

        strSender = currentUserId + "-" + otherUserId;
        strReceiver = otherUserId + "-" + currentUserId;

        binding.txtUserName.setText(userName);

        binding.recyclerPersonalDoctor.setHasFixedSize(true);
        binding.recyclerPersonalDoctor.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new MessageAdapter(messagesList);

        binding.recyclerPersonalDoctor.setAdapter(messagesAdapter);

        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (messagesList.size() > 0) {
                    if (charSequence.length() == 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(0);

                        timer.cancel();
                    } else if (i2 > 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(1);

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(3);
                            }
                        }, 5000);
                    } else if (i1 > 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(2);

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(3);
                            }
                        }, 5000);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        getUserData();

    }

    private void getUserData() {

        /**
         * Code block for fetching Current USer Data
         * */
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    if (sessionManager.loginUserType().equals("Doctor")) {
                        final Doctor mDoc = snapshot.getValue(Doctor.class);
                        if (mDoc != null) {
                            mDoc.setFirebaseId(currentUserId);
                            doctor = mDoc;
                            Log.v("FIREBASE_USER_DATA", "CurrentUSer Doctor" + mDoc.getName());
                        }
                    } else {
                        final User mUser = snapshot.getValue(User.class);
                        if (mUser != null) {
                            mUser.setFirebaseId(currentUserId);
                            user = mUser;
                            Log.v("FIREBASE_USER_DATA", "CurrentUSer USER" + mUser.getName());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /**
         * Code block for fetching Other USer Data
         * */
        DatabaseReference otherUserReference = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);
        otherUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    if (sessionManager.loginUserType().equals("Doctor")) {
                        final User mUser = snapshot.getValue(User.class);
                        if (mUser != null) {
                            mUser.setFirebaseId(currentUserId);
                            user = mUser;
                            Log.v("FIREBASE_USER_DATA", "OtherUSer USER" + mUser.getName());
                        }
                    } else {
                        final Doctor mDoc = snapshot.getValue(Doctor.class);
                        if (mDoc != null) {
                            mDoc.setFirebaseId(currentUserId);
                            doctor = mDoc;
                            Log.v("FIREBASE_USER_DATA", "OtherUSer Doctor" + mDoc.getName());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("online").setValue("true");
        loadMessages();
        initDatabases();
    }

    @Override
    protected void onPause() {
        super.onPause();

        running = false;

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("online").setValue(ServerValue.TIMESTAMP);

        if (messagesList.size() > 0 && messageEditText.getText().length() > 0) {
            FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId).child("typing").setValue(0);
        }

        removeListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri url = data.getData();

            DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId).child(otherUserId).push();
            final String messageId = messageRef.getKey();

            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(otherUserId).push();
            final String notificationId = notificationRef.getKey();

            StorageReference file = FirebaseStorage.getInstance().getReference().child("message_images").child(messageId + ".jpg");
        }
    }

    private void initDatabases() {
        // Initialize/Update realtime other user data such as name and online status

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserId);
        userListener = new ValueEventListener() {
            Timer timer;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    txtv_chatUserName.setText(name);

                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    final String online = dataSnapshot.child("online").getValue().toString();

                } catch (Exception e) {
                    Log.d(TAG, "setDatabase(): usersOtherUserListener exception: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "setDatabase(): usersOtherUserListener failed: " + databaseError.getMessage());
            }
        };
        userDatabase.addValueEventListener(userListener);

        //Check if last message is unseen and mark it as seen with current timestamp

        chatDatabase = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId).child(otherUserId);
        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild("seen")) {
                        long seen = (long) dataSnapshot.child("seen").getValue();

                        if (seen == 0) {
                            chatDatabase.child("seen").setValue(ServerValue.TIMESTAMP);
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "setDatabase(): chatCurrentUserListener exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "setDatabase(): chatCurrentUserListener failed: " + databaseError.getMessage());
            }
        };
        chatDatabase.addValueEventListener(chatListener);
    }

    private void loadMessages() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(REF_CHATS).child(strReceiver);
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messagesList.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Message chat = snapshot.getValue(Message.class);
                            assert chat != null;
                            //if (!Utils.isEmpty(chat.getMessage())) {
                            chat.setId(snapshot.getKey());
                            messagesList.add(chat);
                            //}

                        } catch (Exception ignored) {
                        }
                    }
                }
                try {
                    messagesAdapter.notifyDataSetChanged();

                    binding.recyclerPersonalDoctor.scrollToPosition(messagesList.size() - 1);
                } catch (Exception e) {
                    Log.d(TAG, "loadMessages(): messegesListener exception: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void removeListeners() {
        try {
            chatDatabase.removeEventListener(chatListener);
            chatListener = null;

            userDatabase.removeEventListener(userListener);
            userListener = null;

            messagesDatabase.removeEventListener(messagesListener);
            messagesListener = null;
        } catch (Exception e) {
            Log.d(TAG, "exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMessage() {

        String message = messageEditText.getText().toString();

        if (message.length() == 0) {
            Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();

        } else {
            messageEditText.setText("");

            // Pushing message/notification so we can get keyIds

            DatabaseReference userMessage = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId).child(otherUserId).push();
            String pushId = userMessage.getKey();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(otherUserId).push();
            String notificationId = notificationRef.getKey();

            // "Packing" message

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("type", "text");
            messageMap.put("from", currentUserId);
            messageMap.put("to", otherUserId);
            messageMap.put("timestamp", ServerValue.TIMESTAMP);

            HashMap<String, String> notificationData = new HashMap<>();
            notificationData.put("from", currentUserId);
            notificationData.put("type", "message");

            final String key = Utils.getChatUniqueId();
            reference.child(REF_CHATS).child(strSender).child(key).setValue(messageMap);
            reference.child(REF_CHATS).child(strReceiver).child(key).setValue(messageMap);

            try {
                sendNotification("Notification", message, "user");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void sendMessage(String message) {
        // Pushing message/notification so we can get keyIds

        DatabaseReference userMessage = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId).child(otherUserId).push();
        String pushId = userMessage.getKey();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(otherUserId).push();
        String notificationId = notificationRef.getKey();

        // "Packing" message

        Map messageMap = new HashMap();
        messageMap.put("message", message);
        messageMap.put("type", "text");
        messageMap.put("from", currentUserId);
        messageMap.put("to", otherUserId);
        messageMap.put("timestamp", ServerValue.TIMESTAMP);

        HashMap<String, String> notificationData = new HashMap<>();
        notificationData.put("from", currentUserId);
        notificationData.put("type", "message");

        final String key = Utils.getChatUniqueId();
        reference.child(REF_CHATS).child(strSender).child(key).setValue(messageMap);
        reference.child(REF_CHATS).child(strReceiver).child(key).setValue(messageMap);

        try {
            sendNotification("Notification Calling", message, "user");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void sendNotification(final String username, final String message, final String type) {

        final Data data = new Data(currentUserId, R.drawable.ic_message_text, username, message, getString(R.string.strNewMessage), otherUserId, type);

        final Sender sender = new Sender(data, data, otherUserToken);

        String json = new Gson().toJson(sender);
        Log.e(TAG, "sendNotification: " + json);

        apiService.sendNotification(sender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String b = response.body().string();
                        Log.e(TAG, "onResponse: " + b);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                //assert response.code() != 200 || response.body() != null;
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });

    }
}