package com.hariomgarments.pink.Model;

public class My_cart_model {

    //Data Variables
    private String BasketID;
    private String User_ID;
    private String ProductID;
    private String imageUrl;
    private String name;
    private String Price;
    private String Description;

    //Getters and Setters
    public String getBasketID() {
        return BasketID;
    }

    public void setBasketID(String id) {
        this.BasketID = id;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String id) {
        this.User_ID = id;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String id) {
        this.ProductID = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}
