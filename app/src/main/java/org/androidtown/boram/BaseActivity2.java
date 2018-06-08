package org.androidtown.boram;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sj971 on 2018-06-07.
 */

public class BaseActivity2 extends Activity {

    private static Typeface mTypeface = null;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        //font_1 ->
        //font_2 ->
        //font_3 ->The 외계인설명서
        //font_4 -> 미생체

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/font_3.ttf");
            // 외부폰트 사용
            //mTypeface = Typeface.MONOSPACE;
        }
        //setGlobalFont(getWindow().getDecorView());

        // 또는
        View view = findViewById(android.R.id.content);
        setGlobalFont(view);
    }

    private void setGlobalFont(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int vgCnt = vg.getChildCount();
                for (int i = 0; i < vgCnt; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(mTypeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
}
