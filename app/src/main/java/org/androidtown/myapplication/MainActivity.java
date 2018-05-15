package org.androidtown.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText id;
    EditText pw;
    Button loginBtn;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText)findViewById(R.id.loginId);
        pw = (EditText)findViewById(R.id.loginPw);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        signupBtn = (Button)findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputId = id.getText().toString();
                String inputPw = pw.getText().toString();
                if(inputId.equals("")){
                    Toast.makeText(getApplicationContext(), "please enter your ID", Toast.LENGTH_SHORT).show();
                }
                else if(inputPw.equals("")){
                    Toast.makeText(getApplicationContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(inputId.equals("surim")){
                        if(inputPw.equals("12345")){
                            Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_SHORT).show();

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
                    }
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
