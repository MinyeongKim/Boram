package org.androidtown.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    //습관 이름
    EditText habit_title_input;

    //습관 목표 기간 설정
    TextView fromDate;
    TextView toDate;

    //시작, 마지막 날짜 고르는 달력 위한 버튼
    ImageButton fromDateButton;
    ImageButton toDateButton;

    //습관 빈도수 체크하는 부분
    Spinner frequency_spinner;
    EditText frequency_input;
    String[] frequency_type = {"하루", "일주일", "한달"};

    //습관 타입 설정 라디오 버튼
    RadioGroup habit_type_group;
    RadioButton good;
    RadioButton bad;

    //습관 체크 방법 라디오 버튼
    RadioGroup habit_check_group;
    RadioButton alone;
    RadioButton friend;
    RadioButton otherPerson;

    //등록 버튼
    Button register_button;
    String title;
    String startDate;
    String finishDate;
    String frequency;
    String num; //int로 바꾸고 싶음
    String habitType; //좋은 습관 or 나쁜 습관인데 boolean으로 해도 괜찮을 것 같음
    //근데 바꿀거면 '등록'버튼 누른 후에 값 읽는 부분도 바꿔줘야함!
    String checkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        //습관 제목 받아오는 부분
        habit_title_input=(EditText)findViewById(R.id.habit_title_input);

        //사용자가 설정한 시작, 끝 날짜 보여주는 부분
        //달력에서 선택하면 보여줄 예정
        fromDate =(TextView)findViewById(R.id.fromDate);
        toDate =(TextView)findViewById(R.id.toDate);

        //이 버튼을 누르면 달력을 띄워서 사용자가 날짜를 고르게 하고 -> 이를 위에 텍스트 뷰에 넣어줄 예정
        fromDateButton=(ImageButton)findViewById(R.id.fromDateButton);
        toDateButton=(ImageButton)findViewById(R.id.toDateButton);

        //습관 빈도수를 위한 부분 -> 버튼을 누르면 달, 주, 일이 나와서 선택할 수 있도록 함
        //그러고 나서 몇 번을 실천할 것인지 사용자의 입력을 받음
        frequency_spinner = (Spinner) findViewById(R.id.num_button);
        frequency_input = (EditText)findViewById(R.id.number);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, frequency_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency_spinner.setAdapter(adapter);

        frequency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long id) {
                frequency=frequency_type[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //습관 종류 선택을 읽기 위한 부분
        habit_type_group = (RadioGroup)findViewById(R.id.habit_type_group);
        good = (RadioButton)findViewById(R.id.good_habit);
        bad=(RadioButton)findViewById(R.id.bad_habit);

        //습관 체크 방법을 읽기 위한 부분
        habit_check_group = (RadioGroup)findViewById(R.id.habit_check_group);
        alone = (RadioButton)findViewById(R.id.alone);
        friend = (RadioButton)findViewById(R.id.friend);
        otherPerson = (RadioButton)findViewById(R.id.otherPerson);

        //마지막 등록 버튼
        register_button =(Button)findViewById(R.id.register_button);


        /*
        Action Listener들
         */

        //시작 날짜 선택할 수 있는 달력 띄워주기
        fromDateButton.setOnClickListener(new View.OnClickListener(){

            public  void onClick(View view){

            }
        });

        //끝나는 날짜 선택할 수 있는 달력 띄워주기
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //등록하기 버튼 눌렸을 때 -> 입력 정보들 다 디비에 저장하기
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=habit_title_input.getText().toString();

                startDate=fromDate.getText().toString();
                finishDate=toDate.getText().toString();

                //frequency = frequency_button.getText().toString();
                //num=frequency_input.getText().toString(); -> 한달 or 한주 or 하루에 몇번 실천할 것인지

                int radioID = habit_type_group.getCheckedRadioButtonId();

                if(good.getId() == radioID){
                    habitType="good";
                }

                if(bad.getId()==radioID){
                    habitType="bad";
                }

                radioID = habit_check_group.getCheckedRadioButtonId();

                if(alone.getId()==radioID){
                    checkType="alone";
                }

                if(friend.getId()==radioID){
                    checkType="friend";
                }

                if(otherPerson.getId()==radioID){
                    checkType="otherPerson";
                }

                //사용자가 입력을 제대로 안했을 경우 -> 실행이 안되도록 해야함
                //라디오 버튼을 다 누르지 않았을 경우에 잘 실행이 되나
                //제목을 입력안하는 경우는 제대로 인식하지 못함...ㅠㅠㅠㅠㅠㅠㅠㅠ
                if(title=="" || startDate=="" || finishDate=="" || frequency=="" || habitType==null || checkType==null ){
                    Toast.makeText(getApplicationContext(), "모든 내용을 입력해주세요", Toast.LENGTH_LONG).show();
                }

                else{
                    //사용자의 입력 값 확인 차원, Toast 메세지
                    Toast.makeText(getApplicationContext(), title+" "+startDate+" "+finishDate+" "+
                            frequency+" "+habitType+" "+checkType, Toast.LENGTH_LONG).show();
                }

                //이제 이 값들을 사용자 DB에 넣어줘야함......
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
