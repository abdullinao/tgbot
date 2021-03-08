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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import java.util.Locale;

import static Tgbot.Bot.Utils.commandsUtils.UTILwrongChat;

@Service
public class bot extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(bot.class);
//todo бины в xml
    //todo погода
    //todo какие-нибудь уведомления тпиа манги или нового аниме
    //

    private final String chatId;
    private final String botToken;
    private final String botName;

    public bot(@Value("${bot.token}") String botToken, @Value("${bot.name}") String botName,
               @Value("${bot.chatId}") String chatId) {
        this.botToken = botToken;
        this.botName = botName;
        this.chatId = chatId;
    }

    private static Tgbot.Bot.Handlers.commandsHandler commandsHandler = new commandsHandler();
    private static Tgbot.Bot.Handlers.textHandler textHandler = new textHandler();

    //https://core.telegram.org/bots/api#update
    @PostConstruct
    public void registerBot() {

        try {
            logger.debug("starting bot registration");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot(botToken, botName, chatId));
            logger.debug("bot registration completed successfully");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug("received update: {}", update);
        try {
            Message message = update.getMessage();
            logger.debug("extracted message: {}", message);
            /*бот работает только в 1 чате, по-этому дополнительно проверяем что чатайди = заданому чат айди. */
            if (message != null && message.hasText() && String.valueOf(message.getChatId()).equals(chatId) ||
                    message.getForwardFrom().getId() != null) {
                logger.debug("first IF in onUpdateReceived method passed");
                logger.debug("starting checkUserInBD(message) method");
                checkUserInBD(message);
                try {
                    String incomeMessage = message.getText().toLowerCase(Locale.ROOT);
                    if (incomeMessage.equals("/help")) {

                        logger.debug("/help command execution");
                        sendMsg(message, commandsUtils.UTILhelpText);
                        logger.debug("/help execution completed");

                    } else if (incomeMessage.equals("/top")) {
                        logger.debug("/top command execution");
                        sendMsg(message, commandsHandler.topCommand());
                        logger.debug("/top execution completed");
                    } else if (incomeMessage.equals("/all")) {
                        logger.debug("/all command execution");
                        sendMsg(message, commandsHandler.allCommand());
                        logger.debug("/all execution completed");
                    } else if (incomeMessage.contains("плюс реп")) {
                        logger.debug("плюс реп command execution");
                        sendMsg(message, commandsHandler.changeRepCommand(message, 1));
                        logger.debug("плюс реп execution completed");
                    } else if (incomeMessage.contains("минус реп")) {
                        logger.debug("минус реп command execution");
                        sendMsg(message, commandsHandler.changeRepCommand(message, 0));
                        logger.debug("миннус реп execution completed");
                    }
//                    else if (incomeMessage.equals("/погода")) {
//
//                        sendMsg(message, commandsHandler.getWeather());
//
//                    }
                    else if (incomeMessage.equals("/курс")) {
                        logger.debug("/курс command execution");
                        sendMsg(message, commandsHandler.getCourse());
                        logger.debug("/курс execution completed");
                    }

                } catch (Exception e) {
                    logger.error("error in first IF onUpdateReceived: ", e);
                }
            } else {
                logger.warn("this chat cant be used with bot.");
                sendMsg(message, UTILwrongChat);
            }
        } catch (Exception e) {
            logger.error("error in onUpdateReceived method: ", e);
        }
    }

    public void sendMsg(Message msg, String text) {
        logger.debug("message sending invoked");
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setAction(ActionType.TYPING);
        sendChatAction.setChatId(String.valueOf(msg.getChatId()));

        SendMessage message2Send = new SendMessage();
        message2Send.setChatId(String.valueOf(msg.getChatId()));
        message2Send.setText(text);
        logger.debug("message to send: {}", message2Send);
        try {
            logger.debug("executing typing emulation...");
            execute(sendChatAction); //делает вид что печатает
            logger.debug("executing sending...");
            execute(message2Send); //sending
            logger.debug("sending message completed successfully");
        } catch (TelegramApiException e) {
            logger.error("error in message sending: ", e);

        }
    }


    public void checkUserInBD(Message msg) {
        logger.debug("checking user in bd initiated");
//проверка пользователя на существование в бд\ изменеия логина\фио
        try {
            logger.debug("executing method analyzeIfUserInDB with parameters: {}, {}, {}, {}", msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());

            textHandler.analyzeIfUserInDB(msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());

            logger.debug("executing method analyzeIfUserInDB completed without errors");

        } catch (Exception e) {
            logger.error("error in method checkUserInBD: ", e);
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