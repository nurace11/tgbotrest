package com.nurasick.spring.tgbot.telegramWithSpringTwo.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregatorTest {

    @Test
    public void OneThousandBalanceWithTenPercentAPYInOneYearShouldBeTenThousand() {
        Aggregator aggregator = new Aggregator();
        assertEquals(10000,aggregator.simplePercent(1000,10,1));
//        assertNotEquals();

        // Boolean. but we could do
        assertTrue(aggregator.simplePercent(1000,10, 1) == 10000);
//        assertTrue();
//        assertFalse();

        // For checking for nulls
//        assertNull();
//        assertNotNull();

        // For throws
/*        assertThrows(IllegalArgumentException.class, () -> {
            aggregator.simplePercent(1000, 10, 1);
        });*/
    }

    @Test
    public void NegativeNumbersShouldThrowException() {
        Aggregator aggregator = new Aggregator();

        assertThrows(IllegalArgumentException.class,
                () -> {aggregator.simplePercent(1000, -10, 1);});
    }



}