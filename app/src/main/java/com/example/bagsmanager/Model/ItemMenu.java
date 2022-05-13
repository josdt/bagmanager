package com.example.bagsmanager.Model;

public class ItemMenu {
    public int id;
    public String nameItem;
    public int icon;

    public ItemMenu(int id,String nameItem, int icon) {
        this.id= id;
        this.nameItem = nameItem;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
