package org.androidtown.myapplication;

/**
 * Created by sj971 on 2018-06-06.
 */

public class item {

    private String name;
    private int photo;
    private String withwho;

    public item(int photo,String name, String who) {
        this.name = name;
        this.photo = photo;
        this.withwho=who;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
