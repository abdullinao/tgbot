package Tgbot.Bot.Handlers;

import Tgbot.Bot.Model.userDAO.user;
import Tgbot.Bot.Model.userDAO.userService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class textHandler {
    Logger logger = LoggerFactory.getLogger(textHandler.class);

    userService userService = new userService();

    public void analyzeIfUserInDB(int id, String firstName, String lastName, String login) {
        logger.info("analyzing user in DB with parameters: {}, {}, {}, {}", id, firstName, lastName, login);

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
            userSender.setUserLogin(null);
        }
        logger.debug("made a user mock: {}", userSender);

        user userInBd = new user();
        try {
            logger.debug("checking for user in DB");
            userInBd = userService.findById(id);
            logger.debug("user in database: {}", userInBd);
        } catch (Exception e) {
            logger.error("error on checking for user in DB: ", e);
            e.printStackTrace();
        }

//проверка наличия пользователя в бд если нет создаем

        if (userInBd == null) {
            try {
                logger.debug("user wasnt in DB... Creating...");
                userSender.setReputation(0);
                userService.persist(userSender);
            } catch (Exception e) {
                logger.error("error on creating new user: ", e);
            }
        }//если есть проверяем изменения логина или фио
        else if (!userInBd.getUserFullName().equals(userSender.getUserFullName()) || userInBd.getUserLogin() == null
                || !userInBd.getUserLogin().equals(userSender.getUserLogin())) {
            logger.debug("user was in DB and have a changes in profile! Updating.");
            userInBd.setUserFullName(userSender.getUserFullName());
            userInBd.setUserLogin(userSender.getUserLogin());
            try {
                userService.update(userInBd);
            } catch (Exception e) {
                logger.error("error while updating user in bd: ", e);
            }

        }

    }
}
