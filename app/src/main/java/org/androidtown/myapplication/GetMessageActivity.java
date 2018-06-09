package org.androidtown.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//import org.gcsw.boram.R;

public class GetMessageActivity extends AppCompatActivity {

    ImageView imageLoad;
    RatingBar ratingbar1;
    TextView rating_result1;
    EditText comment_value;
    Button sendButton;

    String comment;
    float rating_value;

    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageLoad = (ImageView) findViewById(R.id.imageLoad);
        rating_result1 = (TextView) findViewById(R.id.rating_result);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingbar);
        sendButton = (Button) findViewById(R.id.sendButton);
        comment_value = (EditText) findViewById(R.id.editTExt);
    }

        /*onNewIntent(getIntent());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference pathReference = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);
        //Glide.with(this).asBitmap().load(pathReference).into(imageLoad);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(getApplicationContext(), "다운로드 성공 : "+ uri, Toast.LENGTH_SHORT).show();
                //inputName.setText(uri.toString());
                //Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(imageLoad);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
            }
        });


        //rating 검사
        ratingbar1.setStepSize((float) 0.5);        //별 색깔이 1칸씩줄어들고 늘어남 0.5로하면 반칸씩 들어감
        ratingbar1.setRating((float) 2.5);      // 처음보여줄때(색깔이 한개도없음) default 값이 0  이다
        ratingbar1.setIsIndicator(false);           //true - 별점만 표시 사용자가 변경 불가 , false - 사용자가 변경가능

        ratingbar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rating_value=rating;
                rating_result1.setText("" + rating);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                comment=comment_value.getText().toString(); //사용자가 입력한 comment

                //별점 값은 rating_value에 들어가 있음

                //전송 해주는 부분 넣으면 됨
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                // extract the extra-data in the Notification
                String msg = extras.getString("NotificationMessage");
                //textView.setText(msg);
                filename = msg;
            }
        }
    }*/

}
