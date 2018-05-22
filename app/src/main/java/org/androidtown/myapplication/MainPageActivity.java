package org.androidtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Iterator;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView userName;
    TextView userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main_page);

        userName = (TextView) findViewById(R.id.UserName);
        userID = (TextView) findViewById(R.id.UserID);

        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //슬라이드 메뉴 회원 이름, 아이디 보여주기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        userName.setText(name);
        String id = bundle.getString("ID");
        userID.setText(id);
        //안되고있음.
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
        String USERID = (String)userID.getText().toString();
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
            startActivity(intent);
        }

        //회원 정보 수정하기
        else if (id == R.id.user_info_change) {

        }

        //앱 설정 변경, Go Application setting page
        else if (id == R.id.app_setting) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

            //finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}