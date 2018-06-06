package org.androidtown.myapplication;

import android.widget.ProgressBar;

/**
 * Created by sj971 on 2018-06-06.
 */

public class item {

    String title;
    int photo;
    String withWho;
    int didNum;
    int willNum;
    String type;

    private String UserID;

    public item(String title1, int photo1){
        this.title=title1;
        this.photo=photo1;
    }

    //item  item1 = new item(title, R.drawable.home, withWho,didNum, willNum,type);
    public item(String title1, int photo1, String habit_check1, int didNum1, int willNum1, String type1, String userid) {
        this.title=title1;
        this.photo=photo1;
        this.withWho = habit_check1;
        this.didNum=didNum1;
        this.willNum=willNum1;
        this.type=type1;
        this.UserID = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getUserID() {
        return UserID;
    }

    public String getWithwho() {
        return withWho;
    }

    public void setWithWho(String withWho){
        this.withWho=withWho;
    }

    public int getDidNum() {
        return photo;
    }

    public void setDidNum(int num) {
        this.didNum = num;
    }

    public int getWillNum() {
        return willNum;
    }

    public void setWillNum(int num) {
        this.willNum = num;
    }
}
