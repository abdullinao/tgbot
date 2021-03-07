package Tgbot.Bot;

import Tgbot.Bot.Model.DAO.user;
import Tgbot.Bot.Model.DAO.userService;

public class textHandler {

    userService userService = new userService();

    public void analyzeIfUserInDB(int id, String firstName, String lastName, String login) {
        user userSender = new user();
        userSender.setId(id);
        if (lastName != null) {
            userSender.setUserFullName(firstName + " " + lastName);
        } else {
            userSender.setUserFullName(firstName);
        }
        if (login != null) {
            userSender.setUserLogin(login);
        } else {
            userSender.setUserLogin("");
        }
        user userInBd = new user();
        try {
            userInBd = userService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

//проверка наличия пользователя в бд если нет создаем
        if (userInBd == null) {
            userSender.setReputation(0);
            userService.persist(userSender);
        }//если есть проверяем изменения логина или фио
        else if (!userInBd.getUserFullName().equals(userSender.getUserFullName())
                || !userInBd.getUserLogin().equals(userSender.getUserLogin())
        ) {
            userInBd.setUserFullName(userSender.getUserFullName());
            userInBd.setUserLogin(userSender.getUserLogin());
            userService.update(userInBd);
        }

    }
}
