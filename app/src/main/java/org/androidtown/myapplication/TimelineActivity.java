package org.androidtown.myapplication;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.LinearLayout;

public class TimelineActivity extends AppCompatActivity {


    //타임라인 activity
    //여기서는 각각의 메모장처럼(버튼으로 구현할 예정) 각각 습관의 이름, 빈도수, 진도율 등등을 보여줌
    //그리고 나서 선택하면 해당 습관을 얼마나 했는지 보여주는 달력을 띄워줄 예정 => 이건 추가적인 부분//

    private final int DYNAMIC_VIEW_ID=0x8000;
    private LinearLayout dynamicLayout;

    int habit_num;
    String[] habit_title=new String[habit_num];
    String[] habit_type = new String[habit_num];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        DB에서 습관 갯수 읽어오는 부분 -> 읽은 갯수==habit_num;
        읽으면서 제목, 빈도수 등 배열 값에다 입력하기
        */

        //일단 디비 연결 전 습관 개수를 4개라고 생각하기
        habit_num=4;
        dynamicLayout=(LinearLayout)findViewById(R.id.dynamicArea);

        for(int i=0; i<=(habit_num-1); i++){
            Button dynamicButton = new Button(this);

            //일단 습관 제목과 타입 임의로 설정 -> 디비 연결되면 갯수 읽을 때, 값 넣어주기
            //habit_title[i]="습관 제목 ";
            //habit_type[i]="습관 타입 ";

            dynamicButton.setId(DYNAMIC_VIEW_ID+i);
            dynamicButton.setText("번호: "+i);

            dynamicLayout.addView(dynamicButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        setupActionBar();
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
