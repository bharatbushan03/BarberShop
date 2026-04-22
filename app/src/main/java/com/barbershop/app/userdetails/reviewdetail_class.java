package com.barbershop.app.userdetails;

public class reviewdetail_class {

    private String cust_name,img1,img2,img3,img4,feedbacktext;
    private Float rating;

    public reviewdetail_class(String cust_name, String img1, String img2, String img3, String img4, String feedbacktext, Float rating) {
        this.cust_name = cust_name;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.feedbacktext = feedbacktext;
        this.rating = rating;
    }

    public reviewdetail_class(){}

    public String getCust_name() {
        return cust_name;
    }

    public String getImg1() {
        return img1;
    }

    public String getImg2() {
        return img2;
    }

    public String getImg3() {
        return img3;
    }

    public String getImg4() {
        return img4;
    }

    public String getFeedbacktext() {
        return feedbacktext;
    }

    public Float getRating() {
        return rating;
    }
}
