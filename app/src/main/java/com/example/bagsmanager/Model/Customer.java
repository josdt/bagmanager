package com.example.bagsmanager.Model;

public class Customer {
    int idUser;
    int idRole;
    String username;
    String password;
    String addressCustommer;
    String email;
    String phone;
    int sex;
    String name;

    public Customer(int idUser, int idRole, String username, String password, String addressCustommer, String email, String phone, int sex, String name) {
        this.idUser = idUser;
        this.idRole = idRole;
        this.username = username;
        this.password = password;
        this.addressCustommer = addressCustommer;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.name = name;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddressCustommer() {
        return addressCustommer;
    }

    public void setAddressCustommer(String addressCustommer) {
        this.addressCustommer = addressCustommer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
