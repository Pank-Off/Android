package com.mailru.weather_app;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class NetworkReceiver extends BroadcastReceiver {

    private int messageId = 1000;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Уведомление")
                .setContentText("Нет подключения к интернету");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!hasConnection()) {
            notificationManager.notify(messageId++, builder.build());
        }
    }

    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = Objects.requireNonNull(cm).getAllNetworks();
        return networks.length != 0;
    }

}
