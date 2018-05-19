package org.androidtown.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    EditText idText;
    EditText pwText;
    Button loginBtn;
    Button signupBtn;

    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;

    String inputId, inputPw;

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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputId = idText.getText().toString();
                inputPw = pwText.getText().toString();

                if (inputId.equals("")) {
                    Toast.makeText(getApplicationContext(), "please enter your ID", Toast.LENGTH_SHORT).show();
                } else if (inputPw.equals("")) {
                    Toast.makeText(getApplicationContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    /*if(inputId.equals("surim")){
                        if(inputPw.equals("12345")){
                            Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();

                            //아이디랑 비밀번호 저장하는 부분
                            sharedPrefernces();
                            //applySharedPreference();

                            //메인 화면 띄워주기
                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                            startActivity(intent);

                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "wrong id", Toast.LENGTH_SHORT).show();
                    }*/
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            Iterator<DataSnapshot> userList = dataSnapshot.getChildren().iterator();
                            while(userList.hasNext()){
                                DataSnapshot data = userList.next();
                                if(data.getKey().equals(inputId)) {
                                    String storedPw = (String)data.child("PW").getValue();
                                    if(storedPw.equals(inputPw)) {
                                        Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();

                                        //메인 화면 띄워주기
                                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                                        startActivity(intent);

                                        finish();

                                        return;
                                    }
                                }
                            }
                            Toast.makeText(getApplicationContext(), "존재하지 않는 아이디", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError){

                        }

                    });
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void sharedPrefernces() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString("UserID", inputId);
        toEdit.putString("Password", inputPw);
        toEdit.commit();

        applySharedPreference();
    }

    private void applySharedPreference() {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        //Toast.makeText(getApplicationContext(),"1111111",Toast.LENGTH_LONG).show();
        if (sh_Pref != null && sh_Pref.contains("Username") && sh_Pref.contains("Password")) {
            //왜 여기 안으로 못 들어오는 것일까...
            //Toast.makeText(getApplicationContext(),"22222",Toast.LENGTH_LONG).show();
            String id = sh_Pref.getString("UserID", "noname");
            String pw = sh_Pref.getString("Password", "nopassword");
            idText.setText(id);
            pwText.setText(pw);
        }
    }
}