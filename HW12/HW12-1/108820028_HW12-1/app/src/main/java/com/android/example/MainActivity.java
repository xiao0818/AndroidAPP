package com.android.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        String toastMessage;
                        if(isChecked){
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);
                            if ((hour == 11 && minute >= 11) || hour > 11){
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            calendar.set(Calendar.HOUR_OF_DAY, 11);
                            calendar.set(Calendar.MINUTE, 11);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);

                            if (alarmManager != null) {
                                if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.KITKAT){
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifyPendingIntent);
                                }
                                else{
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), notifyPendingIntent);
                                }
                            }
                            //Set the toast message for the "on" case.
                            toastMessage = "Alarm On!";
                        } else {
                            mNotificationManager.cancelAll();
                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }
                            //Set the toast message for the "off" case.
                            toastMessage = "Alarm Off!";
                        }
                        //Show a toast to say the alarm is turned on or off.
                        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        createNotificationChannel();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Make a Wish notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies at the closest 11:11 AM");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}