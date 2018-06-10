package org.androidtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

//import org.gcsw.boram.R;

public class FeedbackActivity extends BaseActivity{
    StorageReference spaceRef;
    TextView habitTitle, date, writeDate,comment,feedback_date,feedback_comment;
    RatingBar rate, feedback_rate;
    ImageView user_imageLoad;

    String date_value, writeDate_value,comment_value,feedback_date_value,feedback_comment_value, rate_value, feedback_rate_value;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        habitTitle = (TextView)findViewById(R.id.user_habitTitle);
        date = (TextView)findViewById(R.id.user_date);
        writeDate = (TextView)findViewById(R.id.user_writeDate);
        comment = (TextView)findViewById(R.id.user_comment);
        feedback_date = (TextView)findViewById(R.id.feedback_date);
        feedback_comment = (TextView)findViewById(R.id.feedback_comment);

        rate = (RatingBar)findViewById(R.id.user_rate);
        feedback_rate = (RatingBar)findViewById(R.id.feedback_rate);

        user_imageLoad = (ImageView)findViewById(R.id.user_imageLoad);

        Intent info = getIntent();
        Bundle get_info = info.getExtras();

        String filename = get_info.getString("filename");
        Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
        String user = get_info.getString("UserID");

        final int habitIndex = get_info.getInt("HABITINDEX");
        final int index = get_info.getInt("INDEX");

        String habit_title = get_info.getString("Title");
        habitTitle.setText(habit_title);


        String location ="users/" + user + "/habits/current/" + habitIndex + "/history/"+index;
        Toast.makeText(getApplicationContext(),""+location,Toast.LENGTH_SHORT).show();

        String withWho = get_info.getString("CheckMode");

        if(withWho.equals("friend")){
            String friendID = get_info.getString("FriendID");
        }

        spaceRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + filename);
        Glide.with(this).using(new FirebaseImageLoader()).load(spaceRef).into(user_imageLoad);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users/" + user + "/habits/current/" + habitIndex + "/history/"+index);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                date_value= (String) dataSnapshot.child("DATE").getValue();
                writeDate_value=(String) dataSnapshot.child("WRITETIME").getValue();
                comment_value=(String) dataSnapshot.child("COMMENT").getValue();


                feedback_date_value=(String) dataSnapshot.child("FRIENDWRITETIME").getValue();
                feedback_comment_value=(String) dataSnapshot.child("FRIENDCOMMENT").getValue();

                rate_value=(String) dataSnapshot.child("RATING").getValue();
                feedback_rate_value=(String) dataSnapshot.child("FRIENDRATING").getValue();

                date.setText(date_value);
                writeDate.setText(writeDate_value);
                comment.setText(comment_value);

                feedback_date.setText(feedback_date_value);
                feedback_comment.setText(feedback_comment_value);

                rate.setRating(Float.parseFloat(rate_value));
                feedback_rate.setRating(Float.parseFloat(feedback_rate_value));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
