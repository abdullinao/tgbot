package Tgbot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import Tgbot.Bot.bot;

@SpringBootApplication
public class main {
    public static void main(String[] args) throws TelegramApiException {


        SpringApplication.run(main.class, args);


//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(new bot());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }
}
