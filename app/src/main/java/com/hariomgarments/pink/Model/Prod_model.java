package com.hariomgarments.pink.Model;

import java.io.Serializable;

public class Prod_model implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String price;
    private String Imageurl;

    public Prod_model() {

    }
    public Prod_model(String name, String price, String imageurl) {
        this.name = name;
        this.price = price;
        this.Imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        this.Imageurl = imageurl;
    }


}