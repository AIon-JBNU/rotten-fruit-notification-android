package dev.s0n9.rottenfruitnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public MyFirebaseMessaging() {
        super();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Log.d("FCM TOKEN", task.getResult());
                MyApplication.FCM_TOKEN = task.getResult();
                if (MyApplication.LISTENER != null) {
                    MyApplication.LISTENER.onFCMTokenReload();
                }
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("MessageTest", "Message Received.");
        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            showNotification(notification.getTitle(),
                    notification.getBody());
            Log.d("MessageTest", "title: " + notification.getTitle());
            Log.d("MessageTest", "body: " + notification.getBody());
        }
        // 푸시 메시지 수신 시 할 작업
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // 해당 기기의 토큰이 바뀌었을 때 할 작업
        // 서버로 키 값 전달
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        String channelId = "rotten_fruit_notification";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.popup_logo)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000 })
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "rotten_fruit_notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }
}
