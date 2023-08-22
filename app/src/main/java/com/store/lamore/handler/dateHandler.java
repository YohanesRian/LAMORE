package com.store.lamore.handler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateHandler {
    private String date; //yyyyMMdd

    public dateHandler(){}

    public void setDate(String date){
        this.date = date;
    }

    public void setDate(int date){
        this.date = Integer.toString(date);
    }

    public String getM_Y(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormat = new SimpleDateFormat("MMMM yyyy");
        return dateFormat.format(date);
    }

    public String getDate(){
        return date;
    }

    public int getIntDate(){
        return Integer.parseInt(date);
    }
}
