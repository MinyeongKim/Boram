package org.androidtown.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText id;
    EditText pw;
    EditText name;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        id = (EditText)findViewById(R.id.signupId);
        pw = (EditText)findViewById(R.id.signupPw);
        name = (EditText)findViewById(R.id.signupName);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input값 다 디비에 넣기

                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "signup success, please login", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

//id/pw 중복조사 넣을건가?
//이건 기능 다 완성된 다음에 넣어도 괜찮을듯.