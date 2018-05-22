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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    String UserID;

    //타임라인 activity
    //여기서는 각각의 메모장처럼(버튼으로 구현할 예정) 각각 습관의 이름, 빈도수, 진도율 등등을 보여줌 -> 됨 => 디비랑 이제 연동해서 값 넣어주면 됨
    //그리고 나서 선택하면 해당 습관을 얼마나 했는지 보여주는 달력을 띄워줄 예정 => 이건 추가적인 부분//

    private final int DYNAMIC_VIEW_ID=0x8000;
    private LinearLayout dynamicLayout;

    int habit_num;
    String[] habit_title=new String[habit_num];
    String[] habit_type = new String[habit_num];

    ProgressBar progressBar;

    //디비에서 값 읽을 때마다 넣어주면 됨
    int did_count; //총 몇 번 했는지
    int to_do_count; //총 몇 번 해야 하는지
    int ratio; // did_count / to_do_count => 총 몇 % 했는지

    ImageButton imageButton;
    ListView listView;
    TimelineAdapter adapter;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserID = bundle.getString("ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        listView = (ListView) findViewById(R.id.listView);
        adapter = new TimelineAdapter();

        imageButton=(ImageButton)findViewById(R.id.imageButton);

        /*
        DB에서 습관 갯수 읽어오는 부분 -> 읽은 갯수==habit_num;
        읽으면서 제목, 빈도수 등 배열 값에다 입력하기
        */

        //일단 디비 연결 전 습관 개수를 4개라고 생각하기
        habit_num=10;

        for(int i=0; i<habit_num;i++){
            did_count=i;
            to_do_count=i;
            ratio = i+30*i-10;

            int type1=R.drawable.good_tree;
            int type2 = R.drawable.bad_tree;

            //String type = "R.drawable.home";
            //adapter.addItem(new TimelineItem(제목, 체크 타입, "몇번 했나요? "+did_count+" 몇번 해야하나요? "+to_do_count, progressBar, ratio (진행률), ratio+" %" (프로그레스 바 옆에 몇 %지 보여주기), R.drawable.home =>그림));

            if(i/2==1) {
                adapter.addItem(new TimelineItem("제목", "누구랑 함께? ",
                        "몇번 했나요? " + did_count + " 몇번 해야하나요? " + to_do_count, progressBar, ratio, ratio + " %", type1));
            }

            else{
                adapter.addItem(new TimelineItem("제목", "누구랑 함께? ",
                        "몇번 했나요? " + did_count + " 몇번 해야하나요? " + to_do_count, progressBar, ratio, ratio + " %", type2));
            }

        }

        listView.setAdapter(adapter);

        //editText = (EditText) findViewById(R.id.editText);
        /*
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String mobile = "010-1000-1000";
                int age = 20;

                //adapter.addItem(new TimelineItem(name, mobile, age, R.drawable.ic_launcher_background));
                adapter.notifyDataSetChanged();
            }
        });
        */

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TimelineItem item = (TimelineItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "이름 : " + item.getTitle(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                startActivity(intent);
            }
        });
*/

        imageButton.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckActivity.class);
                startActivity(intent);
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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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