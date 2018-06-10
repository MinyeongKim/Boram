package org.androidtown.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.sql.DriverManager.println;

public class MainPageActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    Handler handler = new Handler(Looper.getMainLooper());
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    //서버키 나중에 비공개로: 디비에 저장해놓고 앱 실행하면 불러오기-->MainActivity에 넣는게 나으려나?
    private String ServerKey;
    private static final String TestMsg = "push message test";

    SharedPreferences auto;

    TextView userName;
    TextView userID;

    String name, id;

    ImageButton menuButton;

    ProgressBar progress;

    RequestQueue queue;

    Toolbar toolbar;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceForPushMsgTest;
    private DatabaseReference databaseReferenceForServerKey;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = auto.edit();

        name=auto.getString("userName","");
        id=auto.getString("inputId","");

        userName = (TextView) findViewById(R.id.UserName);
        userID = (TextView) findViewById(R.id.UserID);

        userName.setText(name);
        userID.setText(id);
*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main_page);

        //progress = (ProgressBar)findViewById(R.id.sampleImage);
        //progress.setProgress(80);

        database = FirebaseDatabase.getInstance();
        //databaseReferenceForServerKey = database.getReference("ServerKey");
        //ServerKey = databaseReferenceForServerKey.getKey();

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


        userName = (TextView) findViewById(R.id.UserName);
        userID = (TextView) findViewById(R.id.UserID);

        //메뉴에 사용자 정보 띄워줄라 했는데 안됨
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");//값은 잘 넘어옴.
        userName.setText(name);
        String id = bundle.getString("ID");

        userName.setText(name);
        userID.setText(id);

        //database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users/" + id);

        //로그인되면 스마트폰 주소 받아오기
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        databaseReference.child("fcmToken").setValue(refreshedToken);

        setContentView(R.layout.activity_main_page);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.sun_icon);
        /*
        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.sun_icon);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 250, 250, true));

            newdrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);
        }
        */
        //toolbar.setNavigationIcon(R.drawable.sun_icon);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        */



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String msg;

        /*
        //푸쉬메세지 버튼 테스트
        Button pushTestBtn = (Button) findViewById(R.id.pushTestBtn);
        pushTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostToFCM("확인해주세요!");
            }
        });
*/

    }

    //슬라이드 메뉴 다시 넣는 부분
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //슬라이드 메뉴에서 메뉴를 선택했을 때
    public boolean onNavigationItemSelected(MenuItem item) {
        String USERID = (String) userID.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("ID", USERID);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //홈으로 가는 버튼. Go to main page
        if (id == R.id.home) {
            // Handle the camera action
        }

        //습관 등록
        else if (id == R.id.enroll_habit) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        //타임라인 보기
        else if (id == R.id.timeline) {
            Intent intent = new Intent(getApplicationContext(), TimelineActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        //회원 정보 수정하기
        else if (id == R.id.user_info_change) {
            Intent intent = new Intent(getApplicationContext(), CheckPWActivity.class);
            startActivity(intent);
        }

        //앱 탈퇴하기
        else if (id == R.id.getout) {
            Intent intent = new Intent(getApplicationContext(), CheckPWActivity.class);
            startActivity(intent);
        }

        //앱 설정 변경, Go Application setting page
        else if (id == R.id.app_setting) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

            //finish();
        }

        //로그아웃
        else if (id == R.id.app_logout) {
            //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
            //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = auto.edit();

            //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
            editor.clear();
            editor.commit();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Utilities.setGlobalFont(drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendPostToFCM(final String message) {
        //databaseReferenceForPushMsgTest = database.getReference("users/" + userID.getText().toString()+"/habits/"+habitIdx+"/FRIENDID");
        databaseReferenceForPushMsgTest = database.getReference("users/" + userID.getText().toString());
        databaseReferenceForPushMsgTest.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //받는 사람 주소
                final String fcmToken = (String) dataSnapshot.child("fcmToken").getValue();
                final String userName = (String) dataSnapshot.child("NAME").getValue();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //FMC 메세지 만들기
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            //notification.put("body", message);
                            notification.put("body", "확인해주세요!");
                            notification.put("title", userID + "님이 인증을 요청했어요~");
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

}