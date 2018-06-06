package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by sj971 on 2018-06-06.
 */

public class SplashActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
