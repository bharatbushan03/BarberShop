package com.barbershop.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class TimeAlarm extends BroadcastReceiver {

    Calendar cal=Calendar.getInstance();


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm=null;

        nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent = PendingIntent.getActivity(context,(int) intent.getExtras().getInt("ID"),
                intent, 0);


        Notification notif = new Notification(R.drawable.ic_baseline_notifications_active_24,
                "Appointment reminder",System.currentTimeMillis());

        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.when=intent.getExtras().getLong("LONG");

        nm.notify(intent.getExtras().getInt("ID"), notif);


        //  context.startActivity(new Intent(context, ReservationListing.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
