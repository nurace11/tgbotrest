package com.nurasick.spring.tgbot.telegramWithSpringTwo.entity;

public class CryptoData {
    private double lprice;
    private String curr1;
    private String curr2;

    public CryptoData(){}

    public CryptoData(String price, String curr1, String curr2) {
        this.lprice = Double.parseDouble(price);
        this.curr1 = curr1;
        this.curr2 = curr2;
    }

    @Override
    public String toString() {
        return "CryptoData{" +
                "price=" + lprice +
                ", curr1='" + curr1 + '\'' +
                ", curr2='" + curr2 + '\'' +
                '}';
    }

    public double getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = Double.parseDouble(lprice);
    }

    public String getCurr1() {
        return curr1;
    }

    public void setCurr1(String curr1) {
        this.curr1 = curr1;
    }

    public String getCurr2() {
        return curr2;
    }

    public void setCurr2(String curr2) {
        this.curr2 = curr2;
    }
}
