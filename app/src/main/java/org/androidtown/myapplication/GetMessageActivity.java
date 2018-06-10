package org.androidtown.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.gcsw.boram.R;

public class GetMessageActivity extends AppCompatActivity {

    Handler handler = new Handler(Looper.getMainLooper());
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    //서버키 나중에 비공개로: 디비에 저장해놓고 앱 실행하면 불러오기
    private String ServerKey;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceForAddress;
    DatabaseReference databaseReferenceForServerKey;

    private FirebaseStorage storage;
    StorageReference storageRef;// = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);
    StorageReference spaceRef;

    String filename;
    String habitTitle;
    String SenderID;
    String HabitIndex;
    String historyIndex;

    TextView titleArea;
    ImageView imageLoad;
    RatingBar ratingbar1;
    TextView rating_result1;
    EditText comment_value;
    Button sendButton;

    String comment;
    float rating_value;

    SimpleDateFormat mFormat_forTime = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        onNewIntent(getIntent());
        //storageRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/");
        //.child("images/" + filename);
        //spaceRef = storageRef.child("images/" + filename);
        spaceRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);
        ;

        database = FirebaseDatabase.getInstance();

        //server key
        databaseReferenceForServerKey = database.getReference("ServerKey");
        databaseReferenceForServerKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ServerKey = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageLoad = (ImageView) findViewById(R.id.imageLoad);
        rating_result1 = (TextView) findViewById(R.id.rating_result);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingbar);
        sendButton = (Button) findViewById(R.id.sendButton);
        comment_value = (EditText) findViewById(R.id.editTExt);
        titleArea = (TextView) findViewById(R.id.habitTitle);

        Glide.with(this).using(new FirebaseImageLoader()).load(spaceRef).into(imageLoad);
        titleArea.setText(habitTitle);

        //rating 검사
        ratingbar1.setStepSize((float) 0.5);        //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
        ratingbar1.setRating((float) 2.5);      // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다
        ratingbar1.setIsIndicator(false);           //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능

        ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating_value = rating;
                rating_result1.setText("" + rating);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                comment = comment_value.getText().toString(); //사용자가 입력한 comment

                //별점 값은 rating_value에 들어가 있음

                //전송 해주는 부분 넣으면 됨
                databaseReference = database.getReference("users/" + SenderID + "/habits/current/" + HabitIndex + "/history");

                String inputRate = String.valueOf(rating_value);

                long pushTime = System.currentTimeMillis();
                Date date_forTime = new Date(pushTime);
                final String writing_time = mFormat_forTime.format(date_forTime);

                //String time = new Date().toString();
                databaseReference.child(historyIndex).child("FRIENDWRITETIME").setValue(writing_time);
                databaseReference.child(historyIndex).child("FRIENDCOMMENT").setValue(comment);
                databaseReference.child(historyIndex).child("FRIENDRATING").setValue(inputRate);

                sendPostToFCM();


                finish();
            }
        });
    }

    private void sendPostToFCM() {
        databaseReferenceForAddress = database.getReference("users/" + SenderID);
        databaseReferenceForAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //받는 사람 주소
                final String fcmToken = (String) dataSnapshot.child("fcmToken").getValue();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //FMC 메세지 만들기
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            notification.put("body", "확인해주세요!");//습관제목?
                            notification.put("title", habitTitle + " 에 대한 피드백이 도착하였습니다!");
                            notification.put("tag", "1");
                            root.put("notification", notification);
                            root.put("to", fcmToken);

                            URL Url = new URL(FCM_MESSAGE_URL);
                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.addRequestProperty("Authorization", "key=" + ServerKey);
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");
                            OutputStream os = conn.getOutputStream();
                            os.write(root.toString().getBytes("utf-8"));
                            os.flush();
                            conn.getResponseCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                // extract the extra-data in the Notification
                String msg1 = extras.getString("NotificationMessage");
                String msg2 = extras.getString("habitTitle");
                String msg3 = extras.getString("userid");
                String msg4 = extras.getString("habitidx");
                String msg5 = extras.getString("historyIndex");
                //textView.setText(msg);
                filename = msg1;
                habitTitle = "<" + msg2 + ">";
                SenderID = msg3;
                HabitIndex = msg4;
                historyIndex = msg5;

            }
        }
    }

}