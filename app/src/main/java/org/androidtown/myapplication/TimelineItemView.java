package org.androidtown.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by sj971 on 2018-05-19.
 */

public class TimelineItemView extends LinearLayout {

    TextView habit_title;
    TextView habit_check;
    TextView habit_count;
    ProgressBar progressBar;
    TextView progress_ratio;
    ImageButton imageButton;

    public TimelineItemView(Context context) {
        super(context);

        init(context);
    }

    public TimelineItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.timeline_item, this, true);

        habit_title = (TextView) findViewById(R.id.habit_title);
        habit_check = (TextView) findViewById(R.id.habit_check);
        habit_count = (TextView) findViewById(R.id.habit_count);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progress_ratio = (TextView) findViewById(R.id.ratio);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
    }

    public void setTitle(String title) {
        habit_title.setText(title);
    }

    public void setHabit_check(String check) {
        habit_check.setText(check);
    }

    public void setHabit_count(String count) {
        habit_count.setText(count);
    }

    public void setImage(int resId) {
        imageButton.setImageResource(resId);
    }

    public void setProgressBar(int rate) {
        progressBar.setProgress(rate);
    }

    public void setRatio(String ratio) {
        progress_ratio.setText(ratio);
    }
}
