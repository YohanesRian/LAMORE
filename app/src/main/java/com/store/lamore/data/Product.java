package com.store.lamore.data;

import java.util.ArrayList;

public class Product {

    private String productName;
    private String price;
    private int img_ref;
    private String size;

    public Product(){
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg_ref(int img_ref) {
        this.img_ref = img_ref;
    }

    public void setSize(String size){
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public int getImg_ref() {
        return img_ref;
    }

    public String getSize(){
        return size;
    }

}
