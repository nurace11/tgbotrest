package com.nurasick.spring.tgbot.telegramWithSpringTwo.utils;

public class Aggregator {

    public double simplePercent(double sum, double percent, int years){
        if(sum < 0 || percent < 0 || years < 0) {
            throw new IllegalArgumentException("No negative numbers");
        }

        return sum * (percent * years);
    }

    public void complexPercent(double sum, double APY, int years) {

    }
}
