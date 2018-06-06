package org.androidtown.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
    ImageView imageView;

    TextView date, comment;
    RatingBar rate;

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

        date=(TextView)findViewById(R.id.date);
        comment=(TextView)findViewById(R.id.comment);
        rate=(RatingBar)findViewById(R.id.rate);

        /*
        habit_title = (TextView) findViewById(R.id.habit_title);
        habit_check = (TextView) findViewById(R.id.habit_check);
        habit_count = (TextView) findViewById(R.id.habit_count);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progress_ratio = (TextView) findViewById(R.id.ratio);
        imageView = (ImageView) findViewById(R.id.imageView);
        */
    }

    public void setDate(String date_value) {
        date.setText(date_value);
    }

    public void setComment(String comment_value) {
        comment.setText(comment_value);
    }

    public void setRate(String rate_Value){
        rate.setRating(3);
    }
}
