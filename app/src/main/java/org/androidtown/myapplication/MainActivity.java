package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends BaseActivity{

    EditText idText;
    EditText pwText;
    Button loginBtn;
    Button signupBtn;

    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    String inputId, inputPw;
    String userID;
    String storedPw;
    String userName;

    String loginId, loginPwd;

    CheckBox checked;

    SharedPreferences auto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idText = (EditText) findViewById(R.id.loginId);
        pwText = (EditText) findViewById(R.id.loginPw);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        checked = (CheckBox) findViewById(R.id.autoLogin);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);

        if(loginId !=null && loginPwd !=null)
        {
            //메인 화면 띄워주기
            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", userName);
            bundle.putString("ID", userID);
            intent.putExtras(bundle);
            startActivity(intent);

            finish();
        }

        //인터넷 연결 안되어있으면 와이파이나 데이터 연결하라는 토스트 띄워주기
        else if(loginId ==null && loginPwd ==null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputId = idText.getText().toString();
                    inputPw = pwText.getText().toString();

                    auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

                    if (inputId.equals("")) {
                        Toast.makeText(getApplicationContext(), "please enter your ID", Toast.LENGTH_SHORT).show();
                    } else if (inputPw.equals("")) {
                        Toast.makeText(getApplicationContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> userList = dataSnapshot.getChildren().iterator();
                                while (userList.hasNext()) {
                                    DataSnapshot data = userList.next();
                                    if (data.getKey().equals(inputId)) {
                                        userID = (String) data.getKey();
                                        storedPw = (String) data.child("PW").getValue();
                                        if (storedPw.equals(inputPw)) {
                                            userName = (String) data.child("NAME").getValue();
                                            Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();

                                            if(checked.isChecked()) {
                                                SharedPreferences.Editor autoLogin = auto.edit();
                                                autoLogin.putString("inputId", inputId);
                                                autoLogin.putString("inputPwd", inputPw);

                                                autoLogin.commit();
                                            }

                                            //메인 화면 띄워주기
                                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("name", userName);
                                            bundle.putString("ID", userID);
                                            intent.putExtras(bundle);
                                            startActivity(intent);

                                            finish();

                                            return;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                                Toast.makeText(getApplicationContext(), "존재하지 않는 아이디", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                        ////////////////////////////////////////
                    }
                }
            });
        }
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}