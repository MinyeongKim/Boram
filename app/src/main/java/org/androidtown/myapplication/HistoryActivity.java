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
import android.widget.LinearLayout;
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
    DatabaseReference databaseReference1,databaseReference2;

    //history
    HistoryAdapter adapter;
    int history_num;

    LinearLayout recordView;

    String startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        list = (ListView) findViewById(R.id.list);
        check = (Button) findViewById(R.id.check);
        recordView = (LinearLayout) findViewById(R.id.record);

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
        /*
        title = bundle.getString("Title");
        didNum = bundle.getInt("Did");
        willNum = bundle.getInt("Will");
        type = bundle.getString("Type");
        */

        database = FirebaseDatabase.getInstance();
        databaseReference1 = database.getReference("users/" + UserID + "/habits/current/" + habitIdx);

        //디비에서 해당 습관 정보를 읽어서 띄워주기
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title = (String) dataSnapshot.child("TITLE").getValue();
                withWho = (String) dataSnapshot.child("CHECKMETHOD").getValue();

                String didString = (String) dataSnapshot.child("DID").getValue();
                didNum = Integer.parseInt(didString);//몇번했나

                String willString = (String) dataSnapshot.child("WILL").getValue();
                willNum = Integer.parseInt(willString);//몇번해야하나

                type = (String) dataSnapshot.child("TYPE").getValue(); //good/bad habit

                startDate = (String) dataSnapshot.child("START").getValue();

                habit_title.setText(title);
                habit_check.setText(habitType);
                habit_count.setText("이때까지 실천한 횟수: " + didNum + "\n총 실천해야하는 횟수: " + willNum);

                double calculate = ((double)didNum / (double)willNum) * 100;
                int progress_value = (int)calculate;
                progress.setProgress(progress_value);
                Toast.makeText(getApplicationContext(),"프로세스 값은 바로~ "+calculate+"  "+progress_value,Toast.LENGTH_SHORT).show();
                ratio.setText(progress_value + " %");

                if (type.equals("good")) {
                    imageView.setImageResource(R.drawable.good_tree);
                } else {
                    imageView.setImageResource(R.drawable.bad_tree);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*
        habit_title.setText(title);
        habit_check.setText(habitType);
        habit_count.setText("이때까지 실천한 횟수: " + didNum + "\n총 실천해야하는 횟수: " + willNum);

        int progress_value = didNum / willNum * 100;
        progress.setProgress(progress_value);
        ratio.setText(progress_value + " %");

        if (type.equals("good")) {
            imageView.setImageResource(R.drawable.good_tree);
        } else {
            imageView.setImageResource(R.drawable.bad_tree);
        }
*/
        //database = FirebaseDatabase.getInstance();
        databaseReference2 = database.getReference("users/" + UserID + "/habits/current/" + habitIdx + "/history/");

        /*adapter = new HistoryAdapter();
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                history_num = (int) dataSnapshot.getChildrenCount();
                String idx = String.valueOf(history_num);

                if (history_num != 0) {
                    recordView.setVisibility(View.VISIBLE);
                }

                Toast.makeText(getApplicationContext(), "" + history_num, Toast.LENGTH_SHORT).show();

                for (int i = 1; i <= history_num; i++) {
                    String histiryIndex = String.valueOf(i);
                    String writingDate = (String) dataSnapshot.child(histiryIndex).child("WRITETIME").getValue();
                    String date = (String) dataSnapshot.child(histiryIndex).child("DATE").getValue();
                    String comment = (String) dataSnapshot.child(histiryIndex).child("COMMENT").getValue();
                    String rate = (String) dataSnapshot.child(histiryIndex).child("RATING").getValue();

                    //float rating_Value= Float.parseFloat(rate);

                    Log.i("date", date);

                    //일단 변수명만 TimelineItem으로 썼어! HistoryItem만들어지면 HistoryItem으로 바꾸면 돼!
                    adapter.addItem(new TimelineItem(date, comment, rate, writingDate));
                }

                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //별점 매기는 부분  + checkActivity
        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        bundle1.putString("STARTDATE", startDate);

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
            view.setRate(item.getRate());
            view.setWritingTime(item.getWritingTime());
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