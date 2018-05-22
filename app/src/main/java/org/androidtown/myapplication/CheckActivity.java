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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.Calendar;

public class CheckActivity extends AppCompatActivity {

    Button button;
    int year, month, day;
    LinearLayout rating_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupActionBar();

        Button button = (Button)findViewById(R.id.button);
        rating_layout=(LinearLayout)findViewById(R.id.rating);

        CalendarView calendar = (CalendarView)findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int y, int m, int d) {
                Toast.makeText(getApplicationContext(), y+"년 "+(m+1)+"월 "+d+"일", Toast.LENGTH_SHORT).show();
                year=y;
                month=m+1;
                day=d;

                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.rating, rating_layout, true);
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