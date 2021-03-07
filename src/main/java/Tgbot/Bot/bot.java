package Tgbot.Bot;


import Tgbot.Bot.Handlers.commandsHandler;
import Tgbot.Bot.Utils.commandsUtils;
import Tgbot.Bot.Handlers.textHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

import static Tgbot.Bot.Utils.commandsUtils.UTILwrongChat;

@Service
public class bot extends TelegramLongPollingBot {

    //@Override
    // public String getBotUsername() {
    // return "springtgbottest66699";
    //  }
//@Override
    //public String getBotToken() {
    //     return "1624746692:AAHslRVLgRHzdv_J8TaYLFhWhRxdkHW_3io";
    //  }
    @Override
    public String getBotUsername() {
        return "KDq3CVM43w_bot";
    }

    @Override

    public String getBotToken() {
        return "916448783:AAEmLGLVEs0vrFm6Ke-Y2949O5fYD6nnYW8";
    }

    private static Tgbot.Bot.Handlers.commandsHandler commandsHandler = new commandsHandler();
    private static Tgbot.Bot.Handlers.textHandler textHandler = new textHandler();
    //https://core.telegram.org/bots/api#update

    private String chatId = "-278344922";//todo вынести чат айди и прочие параметра в отдельный конфиг файл

    @PostConstruct
    public void registerBot(){

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();

            /*бот работает только в 1 чате, по-этому дополнительно проверяем что чатайди = заданому чат айди. */
            if (message != null && message.hasText() && String.valueOf(message.getChatId()).equals(chatId)) {
                checkUserInBD(message);
                try {
                    switch (message.getText()) {
                        case "/help":
                            sendMsg(message, commandsUtils.UTILhelpText);
                            break;
                        case "/top":
                            sendMsg(message, commandsHandler.topCommand());
                            break;
                        case "/all":
                            sendMsg(message, commandsHandler.allCommand());
                            break;
                        case "плюс реп":
                            sendMsg(message, commandsHandler.changeRepCommand(message, 1));
                            break;
                        case "минус реп":
                            sendMsg(message, commandsHandler.changeRepCommand(message, 0));
                            break;
                    }

                } catch (Exception e) {
                    System.out.println("switch ");
                    e.printStackTrace();
                }
            } else {
                sendMsg(message, UTILwrongChat);
            }
        } catch (Exception e) {
            System.out.println("onUpdateReceived ");
            e.printStackTrace();
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

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setAction(ActionType.TYPING);
        sendChatAction.setChatId(String.valueOf(msg.getChatId()));

        SendMessage message2Send = new SendMessage();

        message2Send.setChatId(String.valueOf(msg.getChatId()));
        message2Send.setText(text);
        try {
            execute(sendChatAction); //делает вид что печатает
            execute(message2Send); //sending

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //собирает инфу о отправителе сообщения
    public static void checkUserInBD(Message msg) {
        int userIdFrom = msg.getFrom().getId();
        String userNameFrom = msg.getFrom().getFirstName();
        String userLastNameFrom = msg.getFrom().getLastName();
        String userLoginFrom = msg.getFrom().getUserName();
//создает пользователя в бд если его там нет
        try {
            textHandler.analyzeIfUserInDB(userIdFrom, userNameFrom, userLastNameFrom, userLoginFrom);
        } catch (Exception e) {
            System.out.println("ошибка в методе checkUserInBD");
            e.printStackTrace();
        }
    }
}