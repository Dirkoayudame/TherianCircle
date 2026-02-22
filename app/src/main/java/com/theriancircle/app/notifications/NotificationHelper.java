package com.theriancircle.app.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.theriancircle.app.R;
import com.theriancircle.app.ui.MainActivity;

public final class NotificationHelper {
    public static final String CHANNEL_CHAT = "chat_messages";

    private NotificationHelper() {
    }

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager == null) {
            return;
        }

        NotificationChannel chatChannel = new NotificationChannel(
                CHANNEL_CHAT,
                context.getString(R.string.notif_channel_chat_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        chatChannel.setDescription(context.getString(R.string.notif_channel_chat_desc));
        manager.createNotificationChannel(chatChannel);
    }

    public static void showIncomingChatMessage(Context context, String author, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class)
                .putExtra(MainActivity.EXTRA_OPEN_TAB, MainActivity.TAB_CHAT)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1201,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_CHAT)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(author)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify((int) (System.currentTimeMillis() & 0xfffffff), builder.build());
    }
}
