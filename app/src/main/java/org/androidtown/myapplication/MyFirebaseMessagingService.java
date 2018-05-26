package org.androidtown.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by SurimYuk on 2018-05-23.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        //Map<String, String> bundle = remoteMessage.getData();
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification()!=null){
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification Body: "+body);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.home) //알림 영역에 노출 될 아이콘
                    .setContentTitle(getString(R.string.app_name)) //알림 영역에 노출 될 타이틀
                    .setContentText(body); //Firebase Console에서 사용자가 전달한 메시지 내용
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0x1001, notificationBuilder.build());
        }

    }

}



