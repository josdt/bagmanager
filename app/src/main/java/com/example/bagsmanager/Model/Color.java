package com.example.bagsmanager.Model;

public class Color {
    int idColor;
    String nameColor;

    public Color() {
    }

    public Color(int idColor, String nameColor) {
        this.idColor = idColor;
        this.nameColor = nameColor;
    }

    public int getIdColor() {
        return idColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }
}
