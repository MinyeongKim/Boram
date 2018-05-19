package org.androidtown.myapplication;

import android.widget.ProgressBar;

/**
 * Created by sj971 on 2018-05-19.
 */

public class TimelineItem {

    String title;
    String habit_check;
    String habit_count;
    ProgressBar progressBar;
    String ratio;
    int rate;
    int resId;

    /*
    public TimelineItem(String title, String habit_check, String habit_count, ProgressBar progressBar, String ratio, int home) {
        this.title = title;
        this.habit_check = habit_check;
    }
*/

    public TimelineItem(String title, String habit_check, String habit_count, ProgressBar progressBar, int rateId, String ratio /*,int resId*/) {
        this.title = title;
        this.habit_check = habit_check;
        this.habit_count = habit_count;
        this.progressBar=progressBar;
        this.rate=rateId;
        this.ratio=ratio;
        //this.resId = resId;
    }

    public int getProgressBar() {
        return rate;
    }

    public void setProgressBar(int rate){
        this.rate=rate;
    }

    public String getRatio(){
        return ratio;
    }

    public void setRatio(String ratio){
        this.ratio=ratio;
    }

    public String getHabit_count() {
        return habit_count;
    }

    public void setHabit_count(String habit_count) {
        this.habit_count = habit_count;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getHabit_check() {
        return habit_check;
    }

    public void setHabit_check(String habit_check) {
        this.habit_check = habit_check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
