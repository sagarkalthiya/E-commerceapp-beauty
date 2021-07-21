package com.hariomgarments.pink.Model;

public class Catagory_model  {

    //Data Variables
    private String ProductID;
    private String imageUrl;
    private String name;
    private String Price;
    private String Description;
    private String Catagory;

    //Getters and Setters
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

    public String getCatagory() {
        return Catagory;
    }

    public void setCatagory(String Catagory) {
        this.Catagory = Catagory;
    }
}
