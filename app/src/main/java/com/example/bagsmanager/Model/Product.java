package com.example.bagsmanager.Model;


import androidx.annotation.Nullable;

public class Product  {

    @Nullable
    int idProduct;
    float price;
    String descr;
    String title;
    int idColor;
    int idBrand;
    String image;
    int quantity;

    public Product(int idProduct, float price, String descr, String title, int idColor, int idBrand, String image, int quantity) {
        this.idProduct = idProduct;
        this.price = price;
        this.descr = descr;
        this.title = title;
        this.idColor = idColor;
        this.idBrand = idBrand;
        this.image = image;
        this.quantity = quantity;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
