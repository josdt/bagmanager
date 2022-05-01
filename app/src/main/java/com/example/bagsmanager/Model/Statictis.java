package com.example.bagsmanager.Model;

public class Statictis {
    int year;
    int month;
    float turnover;

    public Statictis() {
    }

    public Statictis(int year, int month, float turnover) {
        this.year = year;
        this.month = month;
        this.turnover = turnover;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getTurnover() {
        return turnover;
    }

    public void setTurnover(float turnover) {
        this.turnover = turnover;
    }
}
