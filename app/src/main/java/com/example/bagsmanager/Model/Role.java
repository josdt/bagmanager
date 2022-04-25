package com.example.bagsmanager.Model;

public class Role {
    int idRole;
    String nameRole;

    public Role() {
    }

    public Role(int idRole, String nameRole) {
        this.idRole = idRole;
        this.nameRole = nameRole;
    }


    public int getIdRole() {
        return idRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }
}
