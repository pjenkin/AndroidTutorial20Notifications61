package com.example.notifications61;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder notification;
    private static final int uuid = 45678;      // unique id for each notification


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // New notification
        notification = new NotificationCompat.Builder(this);
        // Notifications deprecated w/o a channel, but channel assigned in notificationButtonClicked
        notification.setAutoCancel(true);       // disappear automatically when clicked-on
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Called on CLick to build notification & send
    public void notificationButtonClicked(View view)
    {

        // Ready to build notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        // Check to see whether OS/API d'demand a channel on notifications; if so, build same
        // Define a notification channel https://stackoverflow.com/a/47974065/11365317
        // https://developer.android.com/reference/android/app/NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String CHANNEL_ID = "pnj_channel_01";
            // NB CharSequence https://www.baeldung.com/java-char-sequence-string
            CharSequence name = "pnj_channel";
            String Description = "This is pnj's channel (for notifications &c)";
            // capital initial-why? For string as immutable?
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[] {100, 200, 300, 400, 500, 400, 300, 200, 400});
            // delay,vibrate, pause, vibrate, pause, ... https://proandroiddev.com/using-vibrate-in-android-b0e3ef5d5e07
            mChannel.setShowBadge(false);
            // badge on app icon enumerating any notifications received for this app

            notificationManager.createNotificationChannel(mChannel);
            this.notification.setChannelId(notificationManager.getNotificationChannel(CHANNEL_ID).getId());
            // Ab initio, the notification had no channel TODO: maybe improve sequential logic of this
        }

        // Use default icon (for now)
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);
        notification.setTicker("This is the PNJ ticker");       // to appear in the status bar
        notification.setWhen(System.currentTimeMillis());       // appearing in rhs of notification
        notification.setContentTitle("Here's the title of the notification");
        notification.setContentText("Here's the body text of the notification");

        // Send user back to home screen of app on click of notification (simple)
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        // requestCode, intent, flags
        // Give device access to form this intent's actvitity from intents on device
        notification.setContentIntent(pendingIntent);

        // Issue notification (issue: send to device using Notification Manager)
        notificationManager.notify(uuid, notification.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
