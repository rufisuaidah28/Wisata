package com.example.alhuzwiri.wisata;

import java.sql.Timestamp;

public class post{

    public String UserId,Image,desc,namee,Image_tumb;

    public post(String userId, String namee, String desc, String time) {
        this.UserId = userId;
        this.desc = desc;
        this.namee = namee;
    }

//    public post (String UserId, String Image, String desc, String namee,String Image_thumb) {
//        this.UserId = UserId;
//        this.Image = Image;
//        this.desc = desc;
//        this.namee = namee;
//        this.Image_tumb = Image_thumb;
//    }


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNamee() {
        return namee;
    }

    public void setNamee(String namee) {
        this.namee = namee;
    }

    public String getImage_tumb() {
        return Image_tumb;
    }

    public void setImage_tumb(String image_tumb) {
        Image_tumb = image_tumb;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp time;
}




















//public class post {
//    public String UserId,Image,desc,namee,Image_tumb;
//    public Timestamp time;
//    public post(String nama, String nim, String hp){};
//
//
//    public String getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(String userId) {
//        UserId = userId;
//    }
//
//    public String getImage() {
//        return Image;
//    }
//
//    public void setImage(String image) {
//        Image = image;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getNamee() {
//        return namee;
//    }
//
//    public void setNamee(String namee) {
//        this.namee = namee;
//    }
//
//    public String getImage_tumb() {
//        return Image_tumb;
//    }
//
//    public void setImage_tumb(String image_tumb) {
//        Image_tumb = image_tumb;
//    }
//
//    public Timestamp getTimes() {
//        return times;
//    }
//
//    public void setTimes(Timestamp times) {
//        this.times = times;
//    }
//
//    public Timestamp times;
//
//}
