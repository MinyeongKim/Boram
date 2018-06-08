package org.androidtown.boram;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sj971 on 2018-06-05.
 */

public class Utilities extends BaseActivity{
    public static void setGlobalFont(View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();

                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        //Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font_2.tff");
                        ((TextView) v).setTypeface(Typeface.MONOSPACE);
                    }
                    setGlobalFont(v);
                }
            }
        } else {
            Log.e("setGlobalFont", "This is null ");
        }
    }
}
