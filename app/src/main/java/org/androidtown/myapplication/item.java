package org.androidtown.myapplication;

/**
 * Created by sj971 on 2018-06-06.
 */

public class item {

    private String name;
    private int photo;
    private String withwho;
    private String UserID;

    public item(int photo,String name, String who, String UserId) {
        this.name = name;
        this.photo = photo;
        this.withwho=who;
        this.UserID = UserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return UserID;
    }

    public String getWithwho() {
        return withwho;
    }

    public void setWithwho(String withwho) {
        this.withwho = withwho;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
