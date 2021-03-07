package Tgbot.Bot;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import Tgbot.Bot.commandsUtils.*;

import static Tgbot.Bot.commandsHandler.*;

public class bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "springtgbottest66699";
    }

    @Override
    public String getBotToken() {
        return "1624746692:AAHslRVLgRHzdv_J8TaYLFhWhRxdkHW_3io";
    }

    private static commandsHandler commandsHandler = new commandsHandler();
    //https://core.telegram.org/bots/api#update

    private String chatId = "-278344922";//todo вынести чат айди и прочие параметра в отдельный конфиг файл

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            /*бот работает только в 1 чате, по-этому дополнительно проверяем что чатайди = заданому чат айди. */
            if (message != null && message.hasText()/* && String.valueOf(message.getChatId()).equals(chatId) */) {
                try {
                    switch (message.getText()) {
                        case "/help":
                            sendMsg(message, commandsUtils.helpText);
                            break;
                        case "/top":
                            sendMsg(message, commandsHandler.topCommand());
                            break;
                    }

                } catch (Exception e) {
                    System.out.println("errorss " + e);
                }
            } else {
                sendMsg(message, "error, this chat cant be used with this bot");
            }
        } catch (Exception e) {
            System.out.println("error " + e);
        }
        // We check if the update has a message and the message has text
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
//            message.setChatId(String.valueOf(update.getMessage().getChatId()));
//            message.setText("fuck u");
//            try {
//                execute(message); // Call method to send the message
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void sendMsg(Message msg, String text) {

        SendMessage message2Send = new SendMessage();

        message2Send.setChatId(String.valueOf(msg.getChatId()));
        message2Send.setText(text);
        try {
            execute(message2Send); //sending
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}