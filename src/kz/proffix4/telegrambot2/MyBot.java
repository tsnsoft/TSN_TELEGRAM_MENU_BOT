package kz.proffix4.telegrambot2;

import java.util.ArrayList;
import java.util.List;
import static kz.proffix4.telegrambot2.Utils.decode;
import static kz.proffix4.telegrambot2.Utils.encode;
import static kz.proffix4.telegrambot2.Utils.getBTC;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {

    enum EAction { // Класс-перечисление сосотяния действий
        MENU, ENC, DEC, BTC; // Список значений: меню, шифрование, расшифровка, валюта
    }

    private EAction action = EAction.MENU; // Переменная текущего состояния действия

    // Взаимодействие с пользователем
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message user = update.getMessage();
            String text = user.getText();

            // Выбор пунктов меню -------------------------------
            if (action == EAction.MENU) {

                // Выбор пользователем "Шифровать"
                if (text.equalsIgnoreCase("Шифровать")) {
                    action = EAction.ENC;
                    sendText(user, "Что вы хотите зашифровать?");
                    return;
                }
                // Выбор пользователем "Расшифровать"
                if (text.equalsIgnoreCase("Расшифровать")) {
                    action = EAction.DEC;
                    sendText(user, "Что вы хотите расшифровать?");
                    return;
                }
                // Выбор пользователем "Валюта"
                if (text.equalsIgnoreCase("Валюта")) {
                    action = EAction.BTC;
                }
                // Выбор пользователем "/start"
                if (text.equalsIgnoreCase("/start")) {
                    sendMenu(user.getChatId().toString());
                } else {
                    if (action == EAction.MENU) {
                        sendText(user, "\u274c" + " Неизвестная команда, попробуйте еще раз! "+"\u274c");
                    }
                }
            }
            // -----------------------------------------------------

            // Обработка пунктов меню -------------------------------

            // Шифрация текста
            if (action == EAction.ENC) {
                sendText(user, "Зашифрованный текст: " + encode(text));
                sendMenu(user.getChatId().toString());
                return;
            };

            // Расшиифровка текста
            if (action == EAction.DEC) {
                sendText(user, "Расшифрованный текст: " + decode(text));
                sendMenu(user.getChatId().toString());
                return;
            }

            // Валюта
            if (action == EAction.BTC) {
                sendText(user, getBTC());
                sendMenu(user.getChatId().toString());
                return;
            }
            
            // -----------------------------------------------------

        }
    }

    // Посылка текста пользователю
    public void sendText(Message message, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(String.valueOf(message.getChatId()));
        sendMsg.setText(text);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
        }
    }

    // Создание кнопок-действий у пользователя
    public void sendMenu(String chatId) {
        action = EAction.MENU;

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("\u2b07"+" Выберите нужное действие из меню "+"\u2b07");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row;

        // Первая строка меню из 2 кнопок
        row = new KeyboardRow();
        row.add("Шифровать");
        row.add("Расшифровать");
        keyboard.add(row);

        // Вторая строка меню из 1 кнопки
        row = new KeyboardRow();
        row.add("Валюта");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    // Выдача имени нашего бота
    @Override
    public String getBotUsername() {
        return "TSN_PVL_bot";
    }

    // Привязка нашего бота к Telegram
    @Override
    public String getBotToken() {
        return "1616329288:AAHu8rgkTxG1EHsGbESRTznFaCED5Hxz3Bw";
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

}
