package org.androidtown.myapplication;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class RatingActivity extends BaseActivity {

    TextView textView;
    EditText comment;

    Button button;
    RatingBar ratingbar;

    float rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        /*
        textView=(TextView)findViewById(R.id.rating_result);
        comment = (EditText)findViewById(R.id.editTExt);
        button = (Button)findViewById(R.id.button);
        ratingbar=(RatingBar)findViewById(R.id.ratingbar);

        ratingbar.setStepSize((float) 0.5);        //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
        ratingbar.setRating((float) 2.5);      // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다
        ratingbar.setIsIndicator(false);           //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능

        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                textView.setText("평점 : " + rating);

            }
        });
*/
        /*
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               textView.setText("rating: "+rating+"Rating: "+ratingbar.getRating());
            }
        });
*/
        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),rate+"",Toast.LENGTH_LONG).show();
            }
        });
    }
}