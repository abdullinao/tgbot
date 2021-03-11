package Tgbot.Bot.Handlers;

import Tgbot.Bot.Utils.commandsUtils;
import Tgbot.Bot.bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

import static Tgbot.Bot.Utils.commandsUtils.UTILwrongChat;

public class IncomeMessagesHandler extends bot {

    Logger logger = LoggerFactory.getLogger(IncomeMessagesHandler.class);

    private static Tgbot.Bot.Handlers.commandsHandler commandsHandler;
    private static Tgbot.Bot.Handlers.textHandler textHandler;

    //    public IncomeMessagesHandler(Tgbot.Bot.Handlers.commandsHandler commandsHandler, Tgbot.Bot.Handlers.textHandler textHandler) {
//        this.commandsHandler = commandsHandler;
//        this.textHandler = textHandler;
//    }
//
//
    public void setCommandsHandler(Tgbot.Bot.Handlers.commandsHandler commandsHandler) {
        IncomeMessagesHandler.commandsHandler = commandsHandler;
    }

    public void setTextHandler(Tgbot.Bot.Handlers.textHandler textHandler) {
        IncomeMessagesHandler.textHandler = textHandler;
    }

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

            textHandler.analyzeIfUserInDB(msg.getFrom().getId(), msg.getFrom().getFirstName(),
                    msg.getFrom().getLastName(), msg.getFrom().getUserName());

            logger.debug("executing method analyzeIfUserInDB completed without errors");

        } catch (Exception e) {
            logger.error("error in method checkUserInBD: ", e);
        }
    }


}
