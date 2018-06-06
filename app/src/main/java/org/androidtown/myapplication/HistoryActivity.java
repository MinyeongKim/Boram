package org.androidtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView list;
    Button check;

<<<<<<< HEAD
    String id;
    String title;
    String withWho;
    int didNum;
    int willNum;
    String type;
=======
    int habitIdx;
    String habitType;
    String UserID;
>>>>>>> f00eed56a322762b4a0f4fbc9d4fe4b3d730534b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        Intent getintent = getIntent();
        Bundle bundle = getintent.getExtras();
        habitIdx = bundle.getInt("INDEX");
        habitType = bundle.getString("Check_type");
        UserID = bundle.getString("ID");

        list=(ListView)findViewById(R.id.list);
        check=(Button)findViewById(R.id.check);

        /*
                        Bundle bundle = new Bundle();
                bundle.putInt("INDEX", POSITION+1);
                bundle.putString("Check_type", type);
                bundle.putString("ID", UserId);
                bundle.putString("Title", title);
                bundle.putInt("Did",didNum);
                bundle.putInt("Will", willNum);
         */

        //card 뷰가 눌렸을 때, 전달 받은 습관 정보들 출력 ->TimelineView를 이용해서 보여주기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
        title=bundle.getString("Title");
        withWho=bundle.getString("Check_type");
        didNum=bundle.getInt("Did");
        willNum=bundle.getInt("Will");



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
