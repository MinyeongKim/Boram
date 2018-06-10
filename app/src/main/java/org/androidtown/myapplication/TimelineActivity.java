package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.LinkAddress;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimelineActivity extends BaseActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    String UserID;

    //타임라인 activity
    //여기서는 각각의 메모장처럼(버튼으로 구현할 예정) 각각 습관의 이름, 빈도수, 진도율 등등을 보여줌 -> 됨 => 디비랑 이제 연동해서 값 넣어주면 됨
    //그리고 나서 선택하면 해당 습관을 얼마나 했는지 보여주는 달력을 띄워줄 예정 => 이건 추가적인 부분//

    private FirebaseStorage storage;
    StorageReference spaceRef;

    private final int DYNAMIC_VIEW_ID = 0x8000;
    private LinearLayout dynamicLayout;

    int habit_num=0;
    String[] habit_title = new String[habit_num];
    String[] habit_type = new String[habit_num];

    ProgressBar progressBar;

    //디비에서 값 읽을 때마다 넣어주면 됨
    int did_count; //총 몇 번 했는지
    int to_do_count; //총 몇 번 해야 하는지
    int ratio; // did_count / to_do_count => 총 몇 % 했는지

    ImageView imageView;
    ListView listView;
    //TimelineAdapter adapter;
    EditText editText;

    List<item> items;

    String title, withWho, didString, willString, type;
    int didNum, willNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserID = bundle.getString("ID");
        Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_SHORT).show();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users/"+UserID+"/habits/current");

        //listView = (ListView) findViewById(R.id.listView);
        Utilities.setGlobalFont(listView);

        imageView=(ImageView)findViewById(R.id.imageView);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Utilities.setGlobalFont(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                habit_num = (int) dataSnapshot.getChildrenCount();
                String idx = String.valueOf(habit_num);
                //Toast.makeText(getApplication(), idx, Toast.LENGTH_LONG).show();

                int type_value;

                for (int i = 1; i <= habit_num; i++) {
                    String habitIndex = String.valueOf(i);
                    title = (String) dataSnapshot.child(habitIndex).child("TITLE").getValue();
                    withWho = (String) dataSnapshot.child(habitIndex).child("CHECKMETHOD").getValue();

                    didString = (String) dataSnapshot.child(habitIndex).child("DID").getValue();
                    didNum = Integer.parseInt(didString);//몇번했나

                    willString = (String) dataSnapshot.child(habitIndex).child("WILL").getValue();
                    willNum = Integer.parseInt(willString);//몇번해야하나

                    type = (String) dataSnapshot.child(habitIndex).child("TYPE").getValue(); //good/bad habit

                    if(type.equals("good")){
                        type_value=R.drawable.good_habit;
                    }

                    else{
                        type_value=R.drawable.bad_habit;
                    }

                    item item1 = new item(title, type_value, withWho, didNum, willNum, type, UserID);
                    items.add(item1);

                    recyclerView.setAdapter(new cardAdapter(getApplicationContext(), items, R.layout.content_card_time));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //뒤로가는 버튼 생성
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //뒤로가기 버튼이 눌렀을 경우
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}