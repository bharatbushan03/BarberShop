package com.barbershop.app.userdetails;

public class services {
    String service_name;
    String service_price;
    String service_description;
    String service_duration;

    public services(String name, String price, String description, String duration){
        service_name=name;
        service_price=price;
        service_description=description;
        service_duration=duration;
    }

    services(){}

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public String getService_duration() {
        return service_duration;
    }

    public void setService_duration(String service_duration) {
        this.service_duration = service_duration;
    }
}
