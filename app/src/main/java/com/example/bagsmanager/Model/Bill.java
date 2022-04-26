package com.example.bagsmanager.Model;

import java.util.Date;

public class Bill {
    int idBill;
    int idUser;
    Date dateBill;

    public Bill() {
    }

    public Bill(int idBill, int idUser, Date dateBill) {
        this.idBill = idBill;
        this.idUser = idUser;
        this.dateBill = dateBill;
    }

    public int getIdBill() {
        return idBill;
    }

    public int getIdUser() {
        return idUser;
    }

    public Date getDateBill() {
        return dateBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }
}
