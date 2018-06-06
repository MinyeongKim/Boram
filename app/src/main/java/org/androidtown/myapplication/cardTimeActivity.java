package org.androidtown.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class cardTimeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<item> items = new ArrayList<>();
        item[] item = new item[5];
        item[0] = new item(R.drawable.bad_tree, title, withWho, didString, R.drawable.home, "#1", willNum, "alone", "");
        item[1] = new item(R.drawable.bad_tree, title, withWho, didString, R.drawable.home, "#2", willNum, "alone", "");
        item[2] = new item(R.drawable.bad_tree, title, withWho, didString, R.drawable.home, "#3", willNum, "alone", "");
        item[3] = new item(R.drawable.bad_tree, title, withWho, didString, R.drawable.home, "#4", willNum, "alone", "");
        item[4] = new item(R.drawable.bad_tree, title, withWho, didString, R.drawable.home, "#5", willNum, "alone", "");

        for (int i = 0; i < 5; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new cardAdapter(getApplicationContext(), items, R.layout.content_card_time));
        Utilities.setGlobalFont(recyclerView);
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