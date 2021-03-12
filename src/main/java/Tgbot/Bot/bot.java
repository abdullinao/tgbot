package Tgbot.Bot;


import Tgbot.Bot.Handlers.IncomeMessagesHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Service
public class bot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(bot.class);

    //начало инжекта
    private String botToken;
    private String botName;
    private String chatId;
    private Tgbot.Bot.Handlers.IncomeMessagesHandler incomeMessagesHandler;

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
    //конец инжекта

    public bot(String botToken, String botName, String chatId, Tgbot.Bot.Handlers.IncomeMessagesHandler incomeMessagesHandler) {
        this.botToken = botToken;
        this.botName = botName;
        this.chatId = chatId;
        this.incomeMessagesHandler = incomeMessagesHandler;
    }

    public bot() {
    }

    //https://core.telegram.org/bots/api#update

    public void initMethod() {
        logger.debug("OS INFO: {}", System.getProperties().toString());
        try {
            logger.debug("starting bot registration");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot(botToken, botName, chatId, incomeMessagesHandler)); //todo разобраться почему бот не инитится без конструктора
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

            Message response = incomeMessagesHandler.receiveMessage(message, chatId);

            if (response != null) {

                if (response.getText().contains("video:")) {
                    logger.debug("MESSAGES HANDLER RESPONSE CONTAINS PATH TO VIDEO! {}", response.getText());
                    sendResponseVideo(message, response);
                } else {
                    logger.debug("msg hndl response containts text");
                    sendResponse(message, response);
                }


            }
        } catch (Exception e) {
            logger.error("error in onUpdateReceived: ", e);
        }

    }

    public void sendResponse(Message incomeMessage, Message response) {

        logger.debug("message sending invoked");
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setAction(ActionType.TYPING);
        sendChatAction.setChatId(String.valueOf(incomeMessage.getChatId()));

        SendMessage message2Send = new SendMessage();
        message2Send.setChatId(String.valueOf(incomeMessage.getChatId()));
        message2Send.setText(response.getText());
        message2Send.setReplyToMessageId(incomeMessage.getMessageId());
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


    public void sendResponseVideo(Message msg, Message processedResponse) {
        logger.debug("sending video response");
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setAction(ActionType.RECORDVIDEO);
        sendChatAction.setChatId(String.valueOf(msg.getChatId()));
        try {
            execute(sendChatAction);
        } catch (Exception e) {
            logger.error("error in trying to send chat action: {}", e);
        }
        logger.debug("video uploading emulation executed successfully");

        logger.debug("starting video sending");
        SendVideo SendVideo = new SendVideo();
        SendVideo.setChatId(chatId);
        logger.debug("video path set to: {}", processedResponse.getText().substring(processedResponse.getText().lastIndexOf(": ")));
        SendVideo.setVideo(new InputFile(new File("/" + processedResponse.getText().substring(processedResponse.getText().indexOf(" ") + 1))));
        logger.debug("setVideo ok");

        try {
            logger.debug("executing video");
            execute(SendVideo);
            logger.debug("executing video - success");
        } catch (TelegramApiException e) {
            logger.debug("error on executing video: {}", e);
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