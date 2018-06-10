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

import java.util.Date;
import java.util.Iterator;

public class SignupActivity extends BaseActivity {
    EditText id;
    EditText pw;
    EditText name;
    Button registerBtn;

    boolean temp = false;

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
                temp = false;
                //input값 다 디비에 넣기
                //id/pw 중복조사 넣을건가?
                //이건 기능 다 완성된 다음에 넣어도 괜찮을듯.
                if(id.getText().toString().equals("")||pw.getText().toString().equals("")||name.getText().toString().equals("")){
                    Toast.makeText(getApplication(), "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> userList = dataSnapshot.getChildren().iterator();
                            while (userList.hasNext()) {
                                DataSnapshot data = userList.next();
                                if (data.getKey().equals(id.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    temp = true; //존재하는 아이디
                                    return;
                                }
                            }
                            if (!temp) {
                                databaseReference.child(id.getText().toString()).child("ID").setValue(id.getText().toString());
                                databaseReference.child(id.getText().toString()).child("PW").setValue(pw.getText().toString());
                                databaseReference.child(id.getText().toString()).child("NAME").setValue(name.getText().toString());

                                String time = new Date().toString();
                                int timeindex = time.indexOf(" ");
                                time = time.substring(timeindex+1);
                                timeindex = time.indexOf(" ");
                                String month = time.substring(0, timeindex);
                                time = time.substring(timeindex+1);
                                timeindex = time.indexOf(" ");
                                String day = time.substring(0, timeindex);
                                String year = time.substring(time.lastIndexOf(" ")+1);

                                switch(month){
                                    case "Jan": month = "01"; break;
                                    case "Feb": month = "02"; break;
                                    case "Mar": month = "03"; break;
                                    case "Apr": month = "04"; break;
                                    case "May": month = "05"; break;
                                    case "Jun": month = "06"; break;
                                    case "Jul": month = "07"; break;
                                    case "Aug": month = "08"; break;
                                    case "Sep": month = "09"; break;
                                    case "Oct": month = "10"; break;
                                    case "Nov": month = "11"; break;
                                    case "Dec": month = "12"; break;
                                }


                                String joinday = year+"-"+month+"-"+day;
                                Toast.makeText(getApplicationContext(), joinday, Toast.LENGTH_SHORT).show();

                                databaseReference.child(id.getText().toString()).child("JOINDAY").setValue(joinday);

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                                Toast.makeText(getApplicationContext(), "signup success, please login", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }

                ///
            }
        });
    }
}
