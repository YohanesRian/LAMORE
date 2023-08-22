package com.store.lamore.data;

public class History extends Product{
    private String outletName;
    private int date;

    public History(){
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getOutletName() {
        return outletName;
    }

    public int getDate() {
        return date;
    }

}
