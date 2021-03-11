package Tgbot.Bot.Handlers;

import Tgbot.Bot.Handlers.VideoHandler.webmHandler;
import Tgbot.Bot.Model.userDAO.user;
import Tgbot.Bot.Model.userDAO.userService;
import Tgbot.Bot.Utils.commandsUtils;
import Tgbot.Bot.bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

import static Tgbot.Bot.Utils.commandsUtils.UTILwrongChat;

public class IncomeMessagesHandler extends bot {

    private final Logger logger = LoggerFactory.getLogger(IncomeMessagesHandler.class);


    //тут происходит инжект
    private Tgbot.Bot.Handlers.commandsHandler commandsHandler;
    private Tgbot.Bot.Handlers.VideoHandler.webmHandler webmHandler;
    private Tgbot.Bot.Model.userDAO.userService userService;

    public void setWebmHandler(Tgbot.Bot.Handlers.VideoHandler.webmHandler webmHandler) {
        this.webmHandler = webmHandler;
    }

    public void setCommandsHandler(Tgbot.Bot.Handlers.commandsHandler commandsHandler) {
        this.commandsHandler = commandsHandler;
    }

    public void setUserService(Tgbot.Bot.Model.userDAO.userService userService) {
        this.userService = userService;
    }
    //тут инжект кончается

    public String receiveMessage(Message message, String targetChatID) {
        try {
            logger.debug("extracted message: {}", message);
            /*бот работает только в 1 чате, по-этому дополнительно проверяем что чатайди = заданому чат айди. */
            if (message != null && message.hasText() && String.valueOf(message.getChatId()).equals(targetChatID) ||
                    message.getForwardFrom().getId() != null) {
                logger.debug("first IF in onUpdateReceived method passed");
                logger.debug("starting checkUserInBD(message) method");
                checkUserInBD(message);
                try {
                    String incomeMessage = message.getText().toLowerCase(Locale.ROOT);

                    if (incomeMessage.equals("/help")) {
                        logger.debug("/help command execution");
                        return commandsUtils.UTILhelpText;

                    } else if (incomeMessage.equals("/top")) {
                        logger.debug("/top command execution");
                        return commandsHandler.topCommand();

                    } else if (incomeMessage.equals("/all")) {
                        logger.debug("/all command execution");
                        logger.debug("/all execution completed");
                        return commandsHandler.allCommand();

                    } else if (incomeMessage.contains("плюс реп")) {
                        logger.debug("плюс реп command execution");
                        return commandsHandler.changeRepCommand(message, 1);

                    } else if (incomeMessage.contains("минус реп")) {
                        logger.debug("минус реп command execution");
                        return commandsHandler.changeRepCommand(message, 0);

                    } else if (incomeMessage.contains("/rand")) {
                        logger.debug("/rand executed");
                        return commandsHandler.randomCommand(message);

                    } else if (incomeMessage.equals("/курс")) {
                        logger.debug("/курс command execution");
                        return commandsHandler.getCourse();
                    }

                } catch (Exception e) {
                    logger.error("error in first IF onUpdateReceived: ", e);
                    return "error in first IF onUpdateReceived: " + e.toString();
                }
            } else {
                logger.warn("this chat cant be used with bot.");
                return UTILwrongChat;
            }
        } catch (Exception e) {
            logger.error("error in onUpdateReceived method: ", e);
        }
        return null;
    }


    public void checkUserInBD(Message msg) {
        logger.debug("checking user in bd initiated");
//проверка пользователя на существование в бд\ изменеия логина\фио
        try {
            logger.debug("executing method analyzeIfUserInDB with parameters: {}, {}, {}, {}", msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());

            analyzeIfUserInDB(msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());

            logger.debug("executing method analyzeIfUserInDB completed without errors");

        } catch (Exception e) {
            logger.error("error in method checkUserInBD: ", e);
        }
    }

    public void analyzeIfUserInDB(int id, String firstName, String lastName, String login) {
        logger.info("analyzing user in DB with parameters: {}, {}, {}, {}", id, firstName, lastName, login);

        user userSender = new user();
        userSender.setId(id);

        if (lastName != null) {
            userSender.setUserFullName(firstName + " " + lastName);
        } else {
            userSender.setUserFullName(firstName);
        }
        userSender.setUserLogin(login);
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

        } else {
            logger.debug("user profile wasnt changed or something goes wrong");
        }

    }

}
