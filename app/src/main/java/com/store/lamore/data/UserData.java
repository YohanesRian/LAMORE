package com.store.lamore.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserData {
    private String MemberID;
    private String name, email, phone_number, password;
    private long point;

    public UserData(){}

    public void setMemberID(String MemberID){
        this.MemberID = MemberID;
    }

    public void generateMemberID(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmSSSS");
        this.MemberID = dateFormat.format(date);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }

    public String getPassword(){
        return password;
    }

    public String getMemberID(){
        return MemberID;
    }

    public long getPoint() {
        return point;
    }

}
