package org.androidtown.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.Calendar;

public class CheckActivity extends BaseActivity {

    Button button;
    int year, month, day;
    LinearLayout rating_layout;
    String UserID;
    int habitIdx;

    RatingBar ratingbar1;
    TextView rating_result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        Intent check_type = getIntent();
        Bundle get_type = check_type.getExtras();

        final String type = get_type.getString("Check_type");
        UserID = get_type.getString("ID");
        habitIdx = get_type.getInt("INDEX");

        Toast.makeText(getApplicationContext(), type + "/" + UserID + "/" + habitIdx, Toast.LENGTH_SHORT).show();

        rating_result1 = (TextView) findViewById(R.id.rating_result);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingbar);
        button = (Button) findViewById(R.id.button);
        rating_layout = (LinearLayout) findViewById(R.id.rating);

        //rating 검사
        ratingbar1.setStepSize((float) 0.5);        //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
        ratingbar1.setRating((float) 2.5);      // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다
        ratingbar1.setIsIndicator(false);           //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능

        CalendarView calendar = (CalendarView) findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int y, int m, int d) {
                //Toast.makeText(getApplicationContext(), y + "년 " + (m + 1) + "월 " + d + "일", Toast.LENGTH_SHORT).show();
                year = y;
                month = m + 1;
                day = d;

                rating_layout.setVisibility(View.VISIBLE);

            }
        });

        ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating_result1.setText("" + rating);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ID", UserID);
                bundle.putInt("INDEX", habitIdx);

                //디비 history 라인에 값 저장하기 ->별점이랑 코멘트 저장



                switch (type) {
                    //혼자 하는 경우에는 값을 저장한 후 액티비티 종료
                    case "alone":
                        finish();
                        break;

                        //친구랑 하는 경우에는 값을 저장한 후 이미지 로드하는 페이지로 연결
                    case "friend":
                        Intent intent = new Intent(getApplicationContext(), LoadImageActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    //제 3자랑 하는 경우에는 값을 저장한 후 이미지 로드하는 페이지로 연결
                    case "otherPerson":
                        Intent intent2 = new Intent(getApplicationContext(), LoadImageActivity.class);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                        break;

                    default:
                        break;
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