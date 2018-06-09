package org.androidtown.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity {

    ListView list;
    Button check;

    String id;
    String title;
    String withWho;
    int didNum;
    int willNum;
    String type;

    int habitIdx;
    String habitType;
    String UserID;

    String habitTypeGoodBad;

    //습관 정보 출력
    ImageView imageView;
    TextView habit_title, habit_check, habit_count, ratio;
    ProgressBar progress;

    //DB
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //history
    HistoryAdapter adapter;
    int history_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        list = (ListView) findViewById(R.id.list);
        check = (Button) findViewById(R.id.check);

        imageView = (ImageView) findViewById(R.id.imageView);
        habit_title = (TextView) findViewById(R.id.habit_title);
        habit_check = (TextView) findViewById(R.id.habit_check);
        habit_count = (TextView) findViewById(R.id.habit_count);
        ratio = (TextView) findViewById(R.id.ratio);
        progress = (ProgressBar) findViewById(R.id.progress);

        //card 뷰가 눌렸을 때, 전달 받은 습관 정보들 출력 ->TimelineView를 이용해서 보여주기
        final Intent getintent = getIntent();
        Bundle bundle = getintent.getExtras();
        habitIdx = bundle.getInt("INDEX");
        habitType = bundle.getString("Check_type");
        UserID = bundle.getString("ID");
        title = bundle.getString("Title");
        didNum = bundle.getInt("Did");
        willNum = bundle.getInt("Will");
        type = bundle.getString("Type");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users/" + UserID + "/habits/current/" + habitIdx + "/history");

        habit_title.setText(title);
        habit_check.setText(habitType);
        habit_count.setText(didNum + "   " + willNum);

        int progress_value = didNum / willNum;
        progress.setProgress(progress_value);
        ratio.setText(progress_value + " %");


        if (type.equals("good")) {
            imageView.setImageResource(R.drawable.good_tree);
        } else {
            imageView.setImageResource(R.drawable.bad_tree);
        }


        adapter = new HistoryAdapter();
        //이 adapter는 TimelineAdapter기준으로 쓴거니까 나중에 history용으로 바꿔~

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                history_num = (int) dataSnapshot.getChildrenCount();
                String idx = String.valueOf(history_num);

                for (int i = 1; i <= history_num; i++) {
                    String histiryIndex = String.valueOf(i);
                    String date = (String) dataSnapshot.child(histiryIndex).child("DATE").getValue();
                    String comment = (String) dataSnapshot.child(histiryIndex).child("COMMENT").getValue();
                    String rate = (String) dataSnapshot.child(histiryIndex).child("RATING").getValue();

                    //float rating_Value= Float.parseFloat(rate);

                    Log.i("date",date);

                    //일단 변수명만 TimelineItem으로 썼어! HistoryItem만들어지면 HistoryItem으로 바꾸면 돼!
                    adapter.addItem(new TimelineItem(date, comment, rate));
                }

                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //별점 매기는 부분  + checkActivity
        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int totalHistory = 0;
                        totalHistory = (int) dataSnapshot.getChildrenCount();

                        Intent i = new Intent(getApplication(), CheckActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("HISTORYNUM", totalHistory);
                        bundle1.putString("Check_type", habitType);
                        bundle1.putString("ID", UserID);
                        bundle1.putInt("INDEX", habitIdx);
                        i.putExtras(bundle1);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    class HistoryAdapter extends BaseAdapter {
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
            view.setDate(item.getDate());
            view.setComment(item.getComment());
            //view.setRate(item.getRate());
            view.setRate(item.getRate());
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
