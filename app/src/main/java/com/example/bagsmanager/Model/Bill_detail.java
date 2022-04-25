package com.example.bagsmanager.Model;

public class Bill_detail {
    Bill idBill;
    Product idProduct;
    int quantity;
    float price;

    public Bill_detail() {
    }

    public Bill_detail(Bill idBill, Product idProduct, int quantity, float price) {
        this.idBill = idBill;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }

    public Bill getIdBill() {
        return idBill;
    }

    public Product getIdProduct() {
        return idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setIdBill(Bill idBill) {
        this.idBill = idBill;
    }

    public void setIdProduct(Product idProduct) {
        this.idProduct = idProduct;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
