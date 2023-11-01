package com.vat.icare.appointmentBooking;

import static com.vat.icare.utils.Utils.REF_CHATS;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vat.icare.R;
import com.vat.icare.chats.fcm.APIService;
import com.vat.icare.chats.fcm.Data;
import com.vat.icare.chats.fcm.RetroClient;
import com.vat.icare.chats.fcm.Sender;
import com.vat.icare.databinding.ActivityAppointmentBookingBinding;
import com.vat.icare.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentBookingActivity extends AppCompatActivity {
    ActivityAppointmentBookingBinding binding;
    Calendar myCalendar = Calendar.getInstance();
    String otherUserId, otherUserToken, currentUserId;
    String strSender, strReceiver;
    String nameDoctor;
    String namePateint;
    private APIService apiService;
    String FCM_URL = "https://fcm.googleapis.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_booking);

        otherUserId = getIntent().getStringExtra("userId");
        otherUserToken = getIntent().getStringExtra("token");
        nameDoctor = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String speciality = getIntent().getStringExtra("speciality");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.txtNameUser.setText(nameDoctor + "\n" + speciality);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                binding.timePicker.setText(hourOfDay + ":" + minutes);

            }
        }, 0, 0, false);

        strSender = currentUserId + "-" + otherUserId;
        strReceiver = otherUserId + "-" + currentUserId;

        apiService = RetroClient.getClient(FCM_URL).create(APIService.class);

        binding.datePicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AppointmentBookingActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
        binding.btnConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingAppointment(otherUserId, currentUserId,
                        binding.datePicket.getText().toString(),
                        binding.timePicker.getText().toString());
            }
        });
    }


    private void updateLabel() {
        String myFormat = "dd-MMMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.datePicket.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void sendMessage(String message) {
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

    private void sendNotification(final String username, final String message, final String type) {

        final Data data = new Data(currentUserId, R.drawable.ic_message_text, username, message, getString(R.string.strNewMessage), otherUserId, type);

        final Sender sender = new Sender(data, data, otherUserToken);

        String json = new Gson().toJson(sender);
        apiService.sendNotification(sender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String b = response.body().string();
                        finish();
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

    public void bookingAppointment(String doctorId, String pataintId, String date, String time) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(pataintId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    namePateint = dataSnapshot.child("name").getValue(String.class);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userid = firebaseUser.getUid();

                    Map map = new HashMap<>();
                    map.put("Id", doctorId);
                    map.put("doctorId", doctorId);
                    map.put("nameDoctor", nameDoctor);
                    map.put("patientId", pataintId);
                    map.put("patientName", namePateint);
                    map.put("date", date);
                    map.put("time", time);

                    FirebaseDatabase.getInstance().getReference().child("Bookings").child(pataintId).setValue(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AppointmentBookingActivity.this, "Booking Done", Toast.LENGTH_SHORT).show();
                                        sendMessage("Booking Confirm");
                                    } else {
                                        Log.d("TAG", "registerData failed: " + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error here
            }
        });
    }
}