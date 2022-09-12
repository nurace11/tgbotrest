/*
package com.nurasick.spring.tgbot.telegramWithSpringTwo.service;

import com.nurasick.spring.tgbot.telegramWithSpringTwo.config.BotConfig;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.entity.User;
import com.nurasick.spring.tgbot.telegramWithSpringTwo.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Dice;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Slf4j
//@Component
public class TelegramBot extends TelegramLongPollingBot*/
/*implements WebhookBot*//*
 {
    String HELP_TEXT = "HELP TEXT";
    final String BTNCALLBACK_YES = "YES_BUTTON";

    @Autowired
    UserRepository userRepository;

    final BotConfig config;

    User currentUser;

    // Клавиатура с кнопками

    public TelegramBot(BotConfig config) {
        this.config = config;

*/
/*        List<BotCommand> listOfCommands = new ArrayList<>(){{
            add(new BotCommand("/mydata", "show all data stored in this bot"));
            add(new BotCommand("/deletemydata", "deletes all the data about you in this bot"));
        }};*//*


*/
/*        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*//*

    }

    @Override
    public void onUpdateReceived(Update update) {
//        currentUser = null;
        Message userMessage = update.getMessage();

        if(update.hasMessage() && userMessage.hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText.charAt(0) + " bool: " + (messageText.charAt(0) == '/'));
            if(messageText.charAt(0) == '/'){
                switch (messageText) {
                    case "/start":

                        registerNewUser(userMessage);
                        break;

                    case "/help":

                        sendMessage(chatId, HELP_TEXT);
                        break;

                    case "/mydata":

                        commandMyData(chatId);
                        break;

                    case "/register":

                        register(chatId);
                        break;

                    default:

                        sendMessage(chatId, "Sorry command was not recognized");
                        break;
                }
            } else {
                short code;

                switch (messageText) {
                    case "Атаковать":
                    case "Attack":

                        code = 1;
                        sendMessage(chatId, "Вы напали на [entity_name]. Выберите что делать", getReplyMarkup(code));
                        break;

                    case "Kill creep":
                        sendMessage(chatId, "Вы убили крипа. Вы получили [gold] золота", getReplyMarkup((short)0));
                        break;

                    case "Damage creep":
                        code = 1;
                        sendMessage(chatId, "Вы ранили крипа. Было нанесено [damage]. " +
                                "\nВыберите что делать дальше ", getReplyMarkup(code));

                    default:
                        // нужно как-то скрывыать клавиатуру
                        break;
                }
            }
        } else if(update.hasCallbackQuery()){ // callBackQuery получаем из метода register
            // У каждого сообщеня есть айди, зная его мы можем его редактировать
            String data = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            String text = "";
            EditMessageText message = new EditMessageText();
            message.setChatId(String.valueOf(chatId));
            // Указываем что данный текст не просто должно быть отправлено, а замененов сообщение с определнным id
            message.setMessageId((int)messageId);
            switch (data) {
                case BTNCALLBACK_YES:

                    text = "Your pressed YES button";
                    break;

                case "NO_BUTTON":

                    text = "Your pressed NO button";
                    break;

                case "CANCEL_BUTTON":

                    text = "Your pressed CANCEL button";
                    break;
            }
            message.setText(text);

            try {
                execute(message);
            } catch (TelegramApiException e){
//                log.error("editMessageText error " + e);
            }

        }
    }

    public void sendMessage(Message m) {
        SendMessage message = new SendMessage(String.valueOf(m.getChatId()), m.getText());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(TelegramBot.class.getName() + " Method sendMessage(Message m) Error occurred. " + e);
        }
    }

    public void sendMessage(Message m, String text) {
        SendMessage message = new SendMessage(String.valueOf(m.getChatId()), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(TelegramBot.class.getName() + " Method sendMessage(Message m, String text) Error occurred. " + e);
        }
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(TelegramBot.class.getName() + " Method sendMessage() Error occurred. " + e);
        }
    }

    public void sendMessage(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(TelegramBot.class.getName() + " Method sendMessage() Error occurred. " + e);
        }
    }

    public void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you want to register?");

        InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();

        // таблица
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        // строка
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        // кнопки (колонны)
        InlineKeyboardButton btnYes = new InlineKeyboardButton();
        btnYes.setText("Yes");
        btnYes.setCallbackData(BTNCALLBACK_YES);

        var btnNo = new InlineKeyboardButton();
        btnNo.setText("No");
        btnNo.setCallbackData("NO_BUTTON");

        rowInLine.add(btnYes);
        rowInLine.add(btnNo);

        // строка два
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();

        var btnCancel = new InlineKeyboardButton("Cancel");
        btnCancel.setCallbackData("CANCEL_BUTTON");

        rowInLine2.add(btnCancel);

        // добавление строк в таблицу
        rowsInline.add(rowInLine);
        rowsInline.add(rowInLine2);

        // вставка этой таблицы в маркап
        inlineMarkup.setKeyboard(rowsInline);

        // добавление кнопок в сообщение
        message.setReplyMarkup(inlineMarkup);


        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error("register error " + e);
        }
    }

    public ReplyKeyboardMarkup getReplyMarkup(short code){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        switch (code) {
            case 0:

                break;
            case 1:
                KeyboardRow row = new KeyboardRow();
                row.add("Damage creep");
                row.add("Kill creep");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Stop");
                row.add("Health");
                row.add("Next location");
                keyboardRows.add(row);

                keyboardMarkup.setKeyboard(keyboardRows);
                break;
        }
        return keyboardMarkup;
    }

    public void registerNewUser(Message msg) {
        if(userRepository.findById(msg.getChatId()).isEmpty()){
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUsername(chat.getUserName());
            user.setRegisterDate(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: " + user);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void commandMyData(long chatId) {
        User u = null;
        Optional<User> optional = userRepository.findById(chatId);
        if(optional.isPresent()) {
            u = optional.get();
        }

        String text = "We store " +
                "\n\nchatId: " + u.getChatId() + "" +
                "\n\nFirst name: " + u.getFirstName() + "" +
                "\n\nLast name: " + u.getLastName() + "" +
                "\n\nUsername: " + u.getUsername() + "" +
                "\n\nRegister date: " + u.getRegisterDate().toString();
        sendMessage(chatId, text);
    }

    public void commandDeleteMyData(long chatId) {
        userRepository.deleteById(chatId);

        sendMessage(chatId, "Your data has been deleted! \n\nSend /start to start using our bot again ");
    }



}
*/
