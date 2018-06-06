package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.LinkAddress;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TimelineAdapter adapter;
    EditText editText;

    List<item> items;

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

        /*
        DB에서 습관 갯수 읽어오는 부분 -> 읽은 갯수==habit_num;
        읽으면서 제목, 빈도수 등 배열 값에다 입력하기
        */

        /*


        List<item> items = new ArrayList<>();
        item[] item = new item[5];
        item[0] = new item(R.drawable.home, "#1");
        item[1] = new item(R.drawable.home, "#2");
        item[2] = new item(R.drawable.home, "#3");
        item[3] = new item(R.drawable.home, "#4");
        item[4] = new item(R.drawable.home, "#5");

        for (int i = 0; i < 5; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new cardAdapter(getApplicationContext(), items, R.layout.content_card_time));
        Utilities.setGlobalFont(recyclerView);
         */

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Utilities.setGlobalFont(recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                habit_num = (int)dataSnapshot.getChildrenCount();
                String idx = String.valueOf(habit_num);
                //Toast.makeText(getApplication(), idx, Toast.LENGTH_LONG).show();

                int type1=R.drawable.good_tree;
                int type2 = R.drawable.bad_tree;

                for(int i=1; i <= habit_num;i++){
                    String habitIndex = String.valueOf(i);
                    String title = (String)dataSnapshot.child(habitIndex).child("TITLE").getValue();
                    String withWho = (String)dataSnapshot.child(habitIndex).child("CHECKMETHOD").getValue();
                    String didString = (String)dataSnapshot.child(habitIndex).child("DID").getValue();
                    int didNum = Integer.parseInt(didString);//몇번했나
                    String willString = (String)dataSnapshot.child(habitIndex).child("WILL").getValue();
                    int willNum = Integer.parseInt(willString);//몇번해야하나
                    String type = (String)dataSnapshot.child(habitIndex).child("TYPE").getValue();

                    item  item1 = new item(R.drawable.bad_tree, title, withWho, UserID);
                    items.add(item1);
                }

                /*Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", UserID);
                intent.putExtras(bundle);
                startActivity(intent);*/

                //Toast.makeText(getApplication(), "finish", Toast.LENGTH_LONG).show();
                //listView.setAdapter(adapter);

                recyclerView.setAdapter(new cardAdapter(getApplicationContext(), items, R.layout.content_card_time));
                Utilities.setGlobalFont(recyclerView);
                /*
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        TimelineItem item = (TimelineItem) adapter.getItem(position);
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), CheckActivity.class);

                        Bundle checkType = new Bundle();
                        checkType.putString("Check_type", item.getHabit_check());
                        checkType.putString("ID", UserID);
                        checkType.putInt("INDEX", position+1);
                        intent.putExtras(checkType);
                        startActivity(intent);
                    }
                });
                */

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });
    }

    class TimelineAdapter extends BaseAdapter {
        ArrayList<TimelineItem> items = new ArrayList<TimelineItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(TimelineItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            TimelineItemView view = new TimelineItemView(getApplicationContext());

            TimelineItem item = items.get(position);
            view.setTitle(item.getTitle());
            view.setHabit_check(item.getHabit_check());
            view.setHabit_count(item.getHabit_count());
            view.setProgressBar(item.getProgressBar());
            view.setRatio(item.getRatio());
            view.setImage(item.getResId());

            return view;
        }
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