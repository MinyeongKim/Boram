package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import org.gcsw.boram.R;

public class CheckPW2Activity extends BaseActivity {

    EditText loginPw;
    Button CheckPW;

    SharedPreferences info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        loginPw=(EditText)findViewById(R.id.loginPw);
        CheckPW=(Button)findViewById(R.id.checkPW);

        CheckPW.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String pw1=loginPw.getText().toString();

                info = getSharedPreferences("info", Activity.MODE_PRIVATE);
                String userPW = info.getString("userPw", null);

                if(userPW.equals(pw1)){
                    Intent intent = new Intent(getApplication(), DeleteuserActivity.class);
                    startActivity(intent);

                    finish();
                }

                else{
                    Toast.makeText(getApplicationContext(),"비밀번호를 잘 못 입력하셨습니다.",Toast.LENGTH_SHORT).show();
                }
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