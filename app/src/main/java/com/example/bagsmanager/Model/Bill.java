package com.example.bagsmanager.Model;

import java.util.Date;

public class Bill {
    int idBill;
    int idUser;
    Date dateBill;
    int status;

    public Bill() {
    }

    public Bill(int idBill, int idUser, Date dateBill, int status) {
        this.idBill = idBill;
        this.idUser = idUser;
        this.dateBill = dateBill;
        this.status = status;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getDateBill() {
        return dateBill;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
