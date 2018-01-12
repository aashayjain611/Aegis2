package com.example.android.spitit;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.DEFAULT_LIGHTS;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by aashayjain611 on 12/01/18.
 */

public class MyNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }




}
