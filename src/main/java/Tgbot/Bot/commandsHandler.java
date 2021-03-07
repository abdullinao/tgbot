package Tgbot.Bot;

import Tgbot.Bot.Model.DAO.user;
import Tgbot.Bot.Model.DAO.userDAO;
import Tgbot.Bot.Model.DAO.userService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import Tgbot.Bot.bot.*;

import java.util.ArrayList;

import Tgbot.Bot.commandsUtils.*;

public class commandsHandler {
    userService userService = new userService();

    public String topCommand() {
        ArrayList<user> allUsers = new ArrayList<>();
        try {
            allUsers = userService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        if (allUsers.size() != 0) {
            String topText = "";
            int i = 1;
            for (user allUser : allUsers) {
                topText += i + ". " + allUser.getUser() + " с репутацией: " + allUser.getReputation() + ";\n";
                i++;
            }
            return "топ пользователей по репутации: \n" + topText;
        }

        return commandsUtils.noUsersText;
    }


}
