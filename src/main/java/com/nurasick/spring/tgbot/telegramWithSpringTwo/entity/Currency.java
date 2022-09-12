package com.nurasick.spring.tgbot.telegramWithSpringTwo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD(145), EUR(19), RUB(141), BYN(0), KZT(174);

    private final int id;
}
