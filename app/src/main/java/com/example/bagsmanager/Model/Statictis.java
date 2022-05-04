package com.example.bagsmanager.Model;

public class Statictis {
    int month;
    float turnover;

    public Statictis() {
    }
    public Statictis( int month, float turnover) {

        this.month = month;
        this.turnover = turnover;
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
