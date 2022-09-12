package com.nurasick.spring.tgbot.telegramWithSpringTwo.service;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.Currency;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {
    static CurrencyConversionService getInstance() {
        return new NbrbCurrencyConversionService();
    }

    double getConversionRatio(Currency original, Currency target);
}
