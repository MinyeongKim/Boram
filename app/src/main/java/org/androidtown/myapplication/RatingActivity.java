package org.androidtown.myapplication;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    EditText comment;

    Button button;
    RatingBar rating;

    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        comment = (EditText)findViewById(R.id.editTExt);
        button = (Button)findViewById(R.id.button);
        rating=(RatingBar)findViewById(R.id.ratingbar);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate=rating;
            }
        });

        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),rate+"",Toast.LENGTH_LONG).show();
            }
        });
    }
}