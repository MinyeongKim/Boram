package org.androidtown.boram;

/**
 * Created by sj971 on 2018-05-19.
 */

public class TimelineItem {

    String date;
    String comment;
    //float rate;
    String rate;

    /*
    public TimelineItem(String title, String habit_check, String habit_count, ProgressBar progressBar, String ratio, int home) {
        this.title = title;
        this.habit_check = habit_check;
    }
*/

    /*
    public TimelineItem(String date1, String comment1, float rate1) {
        this.date = date1;
        this.comment = comment1;
        this.rate = rate1;
    }
*/

    public TimelineItem(String date1, String comment1, String rate1) {
        this.date = date1;
        this.comment = comment1;
        this.rate = rate1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    /*
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
*/
    /*
    public String getRatio(){
        return ratio;
    }

    public void setRatio(String ratio){
        this.ratio=ratio;
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
    */
}
