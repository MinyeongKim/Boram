package org.androidtown.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by SurimYuk on 2018-05-23.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final static String TAG = "FCM_MESSAGE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Map<String, String> bundle = remoteMessage.getData();
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) { //&&id==처음받기(사진받기), &&id==평가받기
            String body = remoteMessage.getNotification().getBody();
            String title = remoteMessage.getNotification().getTitle();
            //String ImgFileName = remoteMessage.getNotification().
            //String ImgFileName = remoteMessage.getNotification().getTag();
            String tagValue = remoteMessage.getNotification().getTag();
            int commaIdx = tagValue.indexOf(",");
            String ImgFileName = tagValue.substring(0, commaIdx);

            tagValue = tagValue.substring(commaIdx+1);
            commaIdx = tagValue.indexOf(",");
            String habitTitle = tagValue.substring(0, commaIdx);

            tagValue = tagValue.substring(commaIdx+1);
            commaIdx = tagValue.indexOf(",");
            String userid = tagValue.substring(0, commaIdx);

            tagValue = tagValue.substring(commaIdx+1);
            commaIdx = tagValue.indexOf(",");
            String habitidx = tagValue.substring(0, commaIdx);

            tagValue = tagValue.substring(commaIdx+1);
            String historyIndex = tagValue;

            Log.d(TAG, "Notification Body: " + body);

            //각 다른 역할에 따라 다른 서비스 제공해야함.//requestCode???가 뭐지???
            //Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            //Intent intent = new Intent(getApplicationContext(), SendFeedbackActivity.class);//GetMessageActivity로
            Intent intent = new Intent(getApplicationContext(), GetMessageActivity.class);
            //intent.putExtra("NotificationMessage", "값 전달 받음!");
            intent.putExtra("NotificationMessage", ImgFileName);
            intent.putExtra("habitTitle", habitTitle);
            intent.putExtra("userid", userid);
            intent.putExtra("habitidx", habitidx);
            intent.putExtra("historyIndex", historyIndex);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.icon_v1) //알림 영역에 노출 될 아이콘
                    //.setContentTitle(getString(R.string.app_name)) //알림 영역에 노출 될 타이틀
                    .setContentTitle(title)
                    .setContentText(body) //Firebase Console에서 사용자가 전달한 메시지 내용
                    //.setExtras(bundle);
                    .setVibrate(new long[]{100, 250, 100, 500})//진동효과-->문제 생기면 지워도 됨.
                    .setContentIntent(pendingIntent);//푸시 눌렀을때 반응.
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(0x1001, notificationBuilder.build());
            //notificationManagerCompat.cancel(id); --> 평가 완료 시에 부여되는 id를 통해서 없애야할듯.

        }

    }

}



