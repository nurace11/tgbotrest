package com.nurasick.spring.tgbot.telegramWithSpringTwo.service;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.Currency;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.service.impl.HashMapCurrencyModeService;

public interface CurrencyModeService {

    static CurrencyModeService getInstance() {
        return new HashMapCurrencyModeService();
    }

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currency currency);

    void setTargetCurrency(long chatId, Currency currency);
}
