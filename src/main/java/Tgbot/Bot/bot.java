package Tgbot.Bot;


import Tgbot.Bot.Handlers.commandsHandler;
import Tgbot.Bot.Utils.commandsUtils;
import Tgbot.Bot.Handlers.textHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import java.util.Locale;

import static Tgbot.Bot.Utils.commandsUtils.UTILwrongChat;

@Service
public class bot extends TelegramLongPollingBot {
//todo бины в xml
    //todo погода
    //todo какие-нибудь уведомления тпиа манги или нового аниме
    //

    private final String chatId ;
    private final String botToken;
    private final String botName;

    public bot(@Value("${bot.token}") String botToken,  @Value("${bot.name}") String botName,
    @Value("${bot.chatId}") String chatId)  {
        this.botToken = botToken;
        this.botName=botName;
        this.chatId=chatId;
    }



    private static Tgbot.Bot.Handlers.commandsHandler commandsHandler = new commandsHandler();
    private static Tgbot.Bot.Handlers.textHandler textHandler = new textHandler();

    //https://core.telegram.org/bots/api#update

    @PostConstruct
    public void registerBot() {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot(botToken,botName,chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();

            /*бот работает только в 1 чате, по-этому дополнительно проверяем что чатайди = заданому чат айди. */
            if (message != null && message.hasText() && String.valueOf(message.getChatId()).equals(chatId) ||
                    message.getForwardFrom().getId() != null) {
                checkUserInBD(message);
                try {
                    String incomeMessage = message.getText().toLowerCase(Locale.ROOT);
                    if (incomeMessage.equals("/help")) {

                        sendMsg(message, commandsUtils.UTILhelpText);

                    } else if (incomeMessage.equals("/top")) {

                        sendMsg(message, commandsHandler.topCommand());

                    } else if (incomeMessage.equals("/all")) {

                        sendMsg(message, commandsHandler.allCommand());

                    } else if (incomeMessage.contains("плюс реп")) {

                        sendMsg(message, commandsHandler.changeRepCommand(message, 1));

                    } else if (incomeMessage.contains("минус реп")) {

                        sendMsg(message, commandsHandler.changeRepCommand(message, 0));
                    }
//                    else if (incomeMessage.equals("/погода")) {
//
//                        sendMsg(message, commandsHandler.getWeather());
//
//                    }
                    else if (incomeMessage.equals("/курс")) {

                        sendMsg(message, commandsHandler.getCourse());

                    }

                } catch (Exception e) {
                    System.out.println("commands choser ");
                    e.printStackTrace();
                }
            } else {
                sendMsg(message, UTILwrongChat);
            }
        } catch (Exception e) {
            System.out.println("onUpdateReceived ");
            e.printStackTrace();

        }
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


    public static void checkUserInBD(Message msg) {
//проверка пользователя на существование в бд\ изменеия логина\фио
        try {
            textHandler.analyzeIfUserInDB(msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());
        } catch (Exception e) {
            System.out.println("ошибка в методе checkUserInBD");
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override

    public String getBotToken() {
        return botToken;
    }
}