package org.androidtown.myapplication;

import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by sj971 on 2018-06-06.
 */

public class item {

    String title;
    String file;
    int photo;
    File filePath;
    String withWho;
    int didNum;
    int willNum;
    String type;

    private String UserID;

    private FirebaseStorage storage;
    StorageReference spaceRef;

    public item(String title1, File downloadUrl, String habit_check1, int didNum1, int willNum1, String type1, String userid){
        this.title=title1;
        this.filePath=downloadUrl;
        this.withWho = habit_check1;
        this.didNum=didNum1;
        this.willNum=willNum1;
        this.type=type1;
        this.UserID = userid;
    }

    //item  item1 = new item(title, R.drawable.home, withWho,didNum, willNum,type);
    public item(String title1, String filename, String habit_check1, int didNum1, int willNum1, String type1, String userid) {
        this.title=title1;
        this.file=filename;
        this.withWho = habit_check1;
        this.didNum=didNum1;
        this.willNum=willNum1;
        this.type=type1;
        this.UserID = userid;
    }

    public item(String title1, int  photo1, String habit_check1, int didNum1, int willNum1, String type1, String userid) {
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

    public void setFile(String file){
        this.file=file;
       // spaceRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + file);
    }

    public String getFile(){
        return file;
    }

    public void setFilepath(File fileUri){
        this.filePath=fileUri;
    }

    public File getFilepath(){
        return filePath;
    }
    /*
    spaceRef = storage.getReferenceFromUrl("gs://mobileproject-57744.appspot.com/").child("images/" + item.getFile());

        Glide.with(this).using(new FirebaseImageLoader()).load(spaceRef).into(holder.image);
     */

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
        return didNum;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFilePath() {
        return filePath;
    }
}
