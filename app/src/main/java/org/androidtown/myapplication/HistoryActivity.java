package org.androidtown.myapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //습관 정보 출력
    ImageView imageView;
    TextView habit_title, habit_check,habit_count, ratio;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();
        list=(ListView)findViewById(R.id.list);
        check=(Button)findViewById(R.id.check);

        imageView=(ImageView)findViewById(R.id.imageView);
        habit_title=(TextView)findViewById(R.id.habit_title);
        habit_check=(TextView)findViewById(R.id.habit_check);
        habit_count=(TextView)findViewById(R.id.habit_count);
        ratio=(TextView)findViewById(R.id.ratio);
        progress=(ProgressBar)findViewById(R.id.progress);

        //card 뷰가 눌렸을 때, 전달 받은 습관 정보들 출력 ->TimelineView를 이용해서 보여주기
        Intent getintent = getIntent();
        Bundle bundle = getintent.getExtras();
        habitIdx = bundle.getInt("INDEX");
        habitType = bundle.getString("Check_type");
        UserID = bundle.getString("ID");
        title=bundle.getString("Title");
        didNum=bundle.getInt("Did");
        willNum=bundle.getInt("Will");

        habit_title.setText(title);
        habit_check.setText(habitType);
        habit_count.setText(didNum+"   "+willNum);

        int progress_value=didNum/willNum;
        progress.setProgress(progress_value);
        ratio.setText(progress_value+" %");


        if(habitType.equals("good")){
            imageView.setImageResource(R.drawable.good_tree);
        }

        else{
            imageView.setImageResource(R.drawable.bad_tree);
        }

        //별점 매기는 부분  + checkActivity
        check.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), CheckActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("Check_type", habitType);
                bundle1.putString("ID", UserID);
                bundle1.putInt("INDEX", habitIdx);
                //Toast.makeText(getApplicationContext(), habitType+"/"+UserID+"/"+habitIdx, Toast.LENGTH_SHORT).show();
                i.putExtras(bundle1);
                startActivity(i);
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
