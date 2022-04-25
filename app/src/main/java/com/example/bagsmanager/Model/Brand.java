package com.example.bagsmanager.Model;

public class Brand {
    int idBrand;
    String nameBrand;

    public Brand() {
    }

    public Brand(int idBrand, String nameBrand) {
        this.idBrand = idBrand;
        this.nameBrand = nameBrand;
    }

    public int getIdBrand() {
        return idBrand;
    }

    public String getNameBrand() {
        return nameBrand;
    }

    public void setIdBrand(int idBrand) {
        this.idBrand = idBrand;
    }

    public void setNameBrand(String nameBrand) {
        this.nameBrand = nameBrand;
    }
}
