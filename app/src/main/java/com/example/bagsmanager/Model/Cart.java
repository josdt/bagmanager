package com.example.bagsmanager.Model;

public class Cart {
    Customer idUser;
    Product idProduct;
    int quantity;

    public Cart() {
    }

    public Cart(Customer idUser, Product idProduct, int quantity) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public Customer getIdUser() {
        return idUser;
    }

    public Product getIdProduct() {
        return idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setIdUser(Customer idUser) {
        this.idUser = idUser;
    }

    public void setIdProduct(Product idProduct) {
        this.idProduct = idProduct;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
