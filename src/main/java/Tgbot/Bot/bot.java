package Tgbot.Bot;


import Tgbot.Bot.Handlers.commandsHandler;
import Tgbot.Bot.Handlers.IncomeMessagesHandler;
import Tgbot.Bot.Handlers.textHandler;
import org.springframework.beans.factory.annotation.Value;
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

@Service
public class bot extends TelegramLongPollingBot {
    Logger logger = LoggerFactory.getLogger(bot.class);
    private String botToken;
    private String botName;
    private String chatId;
    private Tgbot.Bot.Handlers.IncomeMessagesHandler incomeMessagesHandler;

    public bot(String botToken, String botName, String chatId, Tgbot.Bot.Handlers.IncomeMessagesHandler incomeMessagesHandler) {
        this.botToken = botToken;
        this.botName = botName;
        this.chatId = chatId;
        this.incomeMessagesHandler = incomeMessagesHandler;
    }
    public bot(){}

    //public bot() {}

    //todo webm 2 mp4

    //https://core.telegram.org/bots/api#update

    public void initMethod() {

        try {
            logger.debug("starting bot registration");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot(botToken,botName,chatId,incomeMessagesHandler)); //todo разобраться почему бот не инитится без конструктора
            logger.debug("bot registration completed successfully");
        } catch (TelegramApiException e) {
            logger.error("ERROR IN REGISTRATION! CHECK API KEY/BOT NAME!", e);

        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.debug("received update: {}", update);
        try {
            Message message = update.getMessage();
            String response = incomeMessagesHandler.receiveMessage(message, chatId);
            if (response != null) {
                sendResponse(message, response);
            }
        } catch (Exception e) {
            logger.error("erroe: ", e);
        }

    }

    public void sendResponse(Message msg, String text) {

        logger.debug("message sending invoked");
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setAction(ActionType.TYPING);
        sendChatAction.setChatId(String.valueOf(msg.getChatId()));

        SendMessage message2Send = new SendMessage();
        message2Send.setChatId(String.valueOf(msg.getChatId()));
        message2Send.setText(text);
        message2Send.setReplyToMessageId(msg.getMessageId());
        try {
            logger.debug("executing typing emulation...");
            execute(sendChatAction); //делает вид что печатает
            logger.debug("message to send: {}", message2Send);
            logger.debug("executing sending...");

            execute(message2Send);
            logger.debug("sending message completed successfully");
        } catch (TelegramApiException e) {
            logger.error("error in message sending: ", e);

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


    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setIncomeMessagesHandler(IncomeMessagesHandler incomeMessagesHandler) {
        this.incomeMessagesHandler = incomeMessagesHandler;
    }
}