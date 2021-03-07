package Tgbot;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import Tgbot.Bot.bot;
public class main {
    public static void main(String[] args) throws TelegramApiException {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
