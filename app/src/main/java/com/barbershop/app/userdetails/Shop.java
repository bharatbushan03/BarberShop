package com.barbershop.app.userdetails;

public class Shop {
    String shop_profile_pic,shop_name,owner_name,shop_mail,shop_password,shop_mobile_no,shop_address;

    public Shop(String shop_profile_pic, String shop_name, String owner_name, String shop_mail, String shop_password, String shop_mobile_no, String shop_address) {
        this.shop_profile_pic = shop_profile_pic;
        this.shop_name = shop_name;
        this.owner_name = owner_name;
        this.shop_mail = shop_mail;
        this.shop_password = shop_password;
        this.shop_mobile_no = shop_mobile_no;
        this.shop_address=shop_address;
    }
public Shop(){}

    public String getShop_profile_pic() {
        return shop_profile_pic;
    }

    public void setShop_profile_pic(String shop_profile_pic) {
        this.shop_profile_pic = shop_profile_pic;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getShop_mail() {
        return shop_mail;
    }

    public void setShop_mail(String shop_mail) {
        this.shop_mail = shop_mail;
    }

    public String getShop_password() {
        return shop_password;
    }

    public void setShop_password(String shop_password) {
        this.shop_password = shop_password;
    }

    public String getShop_mobile_no() {
        return shop_mobile_no;
    }

    public void setShop_mobile_no(String shop_mobile_no) {
        this.shop_mobile_no = shop_mobile_no;
    }
}

