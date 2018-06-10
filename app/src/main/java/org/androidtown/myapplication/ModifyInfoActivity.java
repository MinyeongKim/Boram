package org.androidtown.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyInfoActivity extends BaseActivity {

    TextView loginID;
    EditText  loginPW, userName;
    Button submit;

    SharedPreferences info;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    SharedPreferences auto;

    String loginPwd1,loginName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        loginID=(TextView)findViewById(R.id.loginId);
        loginPW=(EditText)findViewById(R.id.loginPw);
        userName=(EditText)findViewById(R.id.userName);
        submit=(Button)findViewById(R.id.modify);

        info = getSharedPreferences("info", Activity.MODE_PRIVATE);
        final String loginId = info.getString("userId", null);
        String loginName = info.getString("userName", null);

        loginID.setText(loginId);
        userName.setHint(loginName);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users/"+loginId);

        submit.setOnClickListener(new Button.OnClickListener(){

            //회원정보 수정
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(loginPW.getText()) || TextUtils.isEmpty(userName.getText())){
                    Toast.makeText(getApplicationContext(),"모든 정보를 입력하세요",Toast.LENGTH_SHORT).show();
                }

               else{
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String getPW=loginPW.getText().toString();
                            String getName=userName.getText().toString();

                            databaseReference.child("PW").setValue(getPW);
                            databaseReference.child("NAME").setValue(getName);

                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoLogin = auto.edit();

                            autoLogin.putString("inputPwd", getPW);
                            autoLogin.putString("inputName", getName);

                            SharedPreferences info = getSharedPreferences("info", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor Info = info.edit();

                            Info.putString("inputPwd", getPW);
                            Info.putString("inputName", getName);

                            Info.commit();

                            Toast.makeText(getApplication(),"회원 정보를 변경하였습니다",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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
