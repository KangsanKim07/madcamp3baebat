package com.example.chicken;

import java.io.Serializable;

public class Second_order_info implements Serializable {
    private String location;
    private String menu;
    private String pay;
    private String time;
    private String amount;

    public Second_order_info(String location, String menu, String pay, String time, String amount){
        super();
        this.location = location;
        if(menu.equals("프라이드")||menu.equals("드")) this.menu = "후라이드";
        this.menu = menu;
        this.pay = pay;
        this.time = time;
        if(amount.equals("한")) this.amount = "1";
        else if (amount.equals("두")) this.amount= "2";
        else if (amount.equals("세")) this.amount= "3";
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
