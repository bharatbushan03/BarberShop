package com.barbershop.app.userdetails;

public class user {
    String user_profile_pic,user_name,user_mail,user_password,user_mobile_no;

    public user(String user_profile_pic,String user_name, String user_mail, String user_password, String user_mobile_no) {
        this.user_name = user_name;
        this.user_profile_pic=user_profile_pic;
        this.user_mail = user_mail;
        this.user_password = user_password;
        this.user_mobile_no = user_mobile_no;
    }

    public user(){}

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }


}

