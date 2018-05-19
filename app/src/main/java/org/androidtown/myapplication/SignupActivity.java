package org.androidtown.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignupActivity extends AppCompatActivity {
    EditText id;
    EditText pw;
    EditText name;
    Button registerBtn;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        id = (EditText)findViewById(R.id.signupId);
        pw = (EditText)findViewById(R.id.signupPw);
        name = (EditText)findViewById(R.id.signupName);
        registerBtn = (Button)findViewById(R.id.registerBtn);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input값 다 디비에 넣기
                //id/pw 중복조사 넣을건가?
                //이건 기능 다 완성된 다음에 넣어도 괜찮을듯.

                databaseReference.child(id.getText().toString()).child("ID").setValue(id.getText().toString());
                databaseReference.child(id.getText().toString()).child("PW").setValue(pw.getText().toString());
                databaseReference.child(id.getText().toString()).child("NAME").setValue(name.getText().toString());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "signup success, please login", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
