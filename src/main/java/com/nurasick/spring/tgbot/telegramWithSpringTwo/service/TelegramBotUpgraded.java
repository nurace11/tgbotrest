package com.nurasick.spring.tgbot.telegramWithSpringTwo.service;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.Communication;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.config.BotConfig;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.Currency;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@Slf4j
@Component
public class TelegramBotUpgraded extends TelegramLongPollingBot {
    final BotConfig config;

    final Communication communication;

    private final CurrencyModeService currencyModeService = CurrencyModeService.getInstance();
    private final CurrencyConversionService currencyConversionService = CurrencyConversionService.getInstance();

    private final String commandGetBtcPrice = "/btc_price";
    private final String commandSetCurrency = "/set_currency";

    public TelegramBotUpgraded(BotConfig config, Communication communication) {
        this.config = config;
        this.communication = communication;

        List<BotCommand> listOfCommands = new ArrayList<>(){{
            add(new BotCommand(commandGetBtcPrice, "gets btc price from cex.io"));
            add(new BotCommand(commandSetCurrency, "sets currency"));
        }};

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if(update.hasMessage()){
            System.out.println();
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    public void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Currency newCurrency = Currency.valueOf(param[1]);
        System.out.println(Arrays.toString(param)); /////////////
        System.out.println("newCurrency"); ///////////////////
        switch (action) {
            case "ORIGINAL":

                currencyModeService.setOriginalCurrency(message.getChatId(), newCurrency);
                break;

            case "TARGET":

                currencyModeService.setTargetCurrency(message.getChatId(), newCurrency);
                break;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Currency originalCurrency = currencyModeService.getOriginalCurrency(message.getChatId());
        Currency targetCurrency = currencyModeService.getTargetCurrency(message.getChatId());
        for(Currency currency : Currency.values()) {
            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                            .text(getCurrencyButton(originalCurrency, currency))
                            .callbackData("ORIGINAL:" + currency)
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(getCurrencyButton(targetCurrency, currency))
                            .callbackData("TARGET:" + currency)
                            .build()));
        }
        //Метод с помощью которого мы меняем исключительно кнопки (нужно убрать галочки с тех, кого мы не выбрали)
        execute(EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    @SneakyThrows
    public void handleMessage(Message message){
        // handle command
        // Если была отправлена команда / , то она убедт иметь хотябы одну сущность
        if(message.hasText() && message.hasEntities()) {
            // ищем нашу команду если messageEntity = "bot_command"
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if(commandEntity.isPresent()) {
                // обрезаем, так как {/calculate 474as4d6q} тоже считается командой
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                switch (command) {
                    case "calculate":

                        break;

                    case "/set_currency":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        Currency originalCurrency = currencyModeService.getOriginalCurrency(message.getChatId());
                        Currency targetCurrency = currencyModeService.getTargetCurrency(message.getChatId());
                        for(Currency currency : Currency.values()) {
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(getCurrencyButton(originalCurrency, currency))
                                            .callbackData("ORIGINAL: " + currency)
                                            .build(),
                                    InlineKeyboardButton.builder()
                                            .text(getCurrencyButton(targetCurrency, currency))
                                            .callbackData("TARGET: " + currency)
                                            .build()));
                        }
                        execute(SendMessage.builder()
                                .text("Please choose Original and Target currencies")
                                .chatId(message.getChatId().toString())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());
                        break;

                    case commandGetBtcPrice:
                        execute(SendMessage.builder()
                                .text(communication.getPrice() + "")
                                .chatId(message.getChatId().toString())
                                .build());

                }
            }
        }
        if(message.hasText()){
            String messageText = message.getText();
            Optional<Double> value = parseDouble(messageText);
            Currency originalCurrency = currencyModeService.getOriginalCurrency(message.getChatId());
            Currency targetCurrency = currencyModeService.getTargetCurrency(message.getChatId());
            double ratio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
            if(value.isPresent()) {
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(String.format("%4.2f %s is  %4.2f %s", value.get(), originalCurrency, (value.get() * ratio), targetCurrency))
                        .build());
            }
        }
    }

    private Optional<Double> parseDouble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String getCurrencyButton(Currency saved, Currency current) {
        return saved == current ? current + " ✅" : current.name();
        // Если сохраненная равна текущей, выводим текущую с галочкой, иначе просто currenct
    }




    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
