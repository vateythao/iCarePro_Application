package com.vat.icare.chats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
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
import com.vat.icare.R;
import com.vat.icare.calls.VideoCallActivity;
import com.vat.icare.databinding.ActivityPersonnalChatsBinding;
import com.vat.icare.pojo.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PersonalChats extends AppCompatActivity {
    private final String TAG = "CA/PersonalChats";
    ActivityPersonnalChatsBinding binding;
    //Firebase Connection
    private DatabaseReference userDatabase, chatDatabase;
    private ValueEventListener userListener, chatListener;

    // Will handle old/new messages between users
    private Query messagesDatabase;
    private ChildEventListener messagesListener;

    private MessageAdapter messagesAdapter;
    private final List<Message> messagesList = new ArrayList<>();
    AdapterChatting adapterChatting;
    Context context;
    String currentUserId;
    public static String otherUserId;
    public static boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personnal_chats);
        context = this;
        running = true;
        adapterChatting = new AdapterChatting(context);
        otherUserId = getIntent().getStringExtra("useridFirebase");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.recyclerPersonal.setHasFixedSize(true);
        binding.recyclerPersonal.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new MessageAdapter(messagesList);

        binding.recyclerPersonal.setAdapter(messagesAdapter);
        binding.messageEditText.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (messagesList.size() > 0) {
                    if (charSequence.length() == 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(0);

                        timer.cancel();
                    } else if (i2 > 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(1);

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(3);
                            }
                        }, 5000);
                    } else if (i1 > 0) {
                        FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(2);

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(3);
                            }
                        }, 5000);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        binding.imgVideoCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalChats.this, VideoCallActivity.class);
                intent.putExtra("visiter_id", otherUserId);
                startActivity(intent);

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

        if (messagesList.size() > 0 && binding.messageEditText.getText().length() > 0) {
            FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId).child("typing").setValue(0);
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

//            file.putFile(url).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        String imageUrl = task.getResult().getDownloadUrl().toString();
//
//                        Map messageMap = new HashMap();
//                        messageMap.put("message", imageUrl);
//                        messageMap.put("type", "image");
//                        messageMap.put("from", currentUserId);
//                        messageMap.put("to", otherUserId);
//                        messageMap.put("timestamp", ServerValue.TIMESTAMP);
//
//                        HashMap<String, String> notificationData = new HashMap<>();
//                        notificationData.put("from", currentUserId);
//                        notificationData.put("type", "message");
//
//                        Map userMap = new HashMap();
//                        userMap.put("Messages/" + currentUserId + "/" + otherUserId + "/" + messageId, messageMap);
//                        userMap.put("Messages/" + otherUserId + "/" + currentUserId + "/" + messageId, messageMap);
//
//                        userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/message", "You have sent a picture.");
//                        userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/timestamp", ServerValue.TIMESTAMP);
//                        userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/seen", ServerValue.TIMESTAMP);
//
//                        userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/message", "Has send you a picture.");
//                        userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/timestamp", ServerValue.TIMESTAMP);
//                        userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/seen", 0);
//
//                        userMap.put("Notifications/" + otherUserId + "/" + notificationId, notificationData);
//
//                        FirebaseDatabase.getInstance().getReference().updateChildren(userMap, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                sendButton.setEnabled(true);
//
//                                if (databaseError != null) {
//                                    Log.d(TAG, "sendMessage(): updateChildren failed: " + databaseError.getMessage());
//                                }
//                            }
//                        });
//                    }
//                }
//            });
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

                    binding.txtUserName.setText(name);

                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

        chatDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(currentUserId).child(otherUserId);
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
        messagesList.clear();
        // Load/Update all messages between current and other user

        messagesDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId).child(otherUserId);
        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Message message = dataSnapshot.getValue(Message.class);

                    messagesList.add(message);
                    messagesAdapter.notifyDataSetChanged();

                    binding.recyclerPersonal.scrollToPosition(messagesList.size() - 1);
                } catch (Exception e) {
                    Log.d(TAG, "loadMessages(): messegesListener exception: " + e.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadMessages(): messegesListener failed: " + databaseError.getMessage());
            }
        };
        messagesDatabase.addChildEventListener(messagesListener);
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

        String message = binding.messageEditText.getText().toString();

        if (message.length() == 0) {
            Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();

        } else {
            binding.messageEditText.setText("");

            // Pushing message/notification so we can get keyIds

            DatabaseReference userMessage = FirebaseDatabase.getInstance().getReference().child("Messages").child(currentUserId).child(otherUserId).push();
            String pushId = userMessage.getKey();

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

            Map userMap = new HashMap();
            userMap.put("Messages/" + currentUserId + "/" + otherUserId + "/" + pushId, messageMap);
            userMap.put("Messages/" + otherUserId + "/" + currentUserId + "/" + pushId, messageMap);

            userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/message", message);
            userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/timestamp", ServerValue.TIMESTAMP);
            userMap.put("Chat/" + currentUserId + "/" + otherUserId + "/seen", ServerValue.TIMESTAMP);

            userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/message", message);
            userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/timestamp", ServerValue.TIMESTAMP);
            userMap.put("Chat/" + otherUserId + "/" + currentUserId + "/seen", 0);

            userMap.put("Notifications/" + otherUserId + "/" + notificationId, notificationData);

            // Updating database with the new data including message, chat and notification

            FirebaseDatabase.getInstance().getReference().updateChildren(userMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "sendMessage(): updateChildren failed: " + databaseError.getMessage());
                    }
                }
            });
        }
    }
}