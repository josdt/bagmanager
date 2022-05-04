package com.example.bagsmanager.Model;

public class DetailBill {
    int idProduct;
    int quantity;
    float price;

    public DetailBill(int idProduct, int quantity, float price) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }

    public int getidProduct() {
        return idProduct;
    }

    public void setTitle(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
