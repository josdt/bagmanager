package com.example.bagsmanager.Model;

import java.util.Date;

public class Bill {
    int idBill;
    Customer idUser;
    Date dateBill;

    public Bill() {
    }

    public Bill(int idBill, Customer idUser, Date dateBill) {
        this.idBill = idBill;
        this.idUser = idUser;
        this.dateBill = dateBill;
    }

    public int getIdBill() {
        return idBill;
    }

    public Customer getIdUser() {
        return idUser;
    }

    public Date getDateBill() {
        return dateBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public void setIdUser(Customer idUser) {
        this.idUser = idUser;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }
}
