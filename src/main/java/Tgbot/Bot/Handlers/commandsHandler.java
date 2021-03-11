package Tgbot.Bot.Handlers;

import Tgbot.Bot.bot;
import Tgbot.externalAPIs.httpRequestor;
import Tgbot.Bot.Model.oldMoney;
import Tgbot.Bot.Model.userDAO.user;
import Tgbot.Bot.Model.userDAO.userService;
import Tgbot.Bot.Utils.commandsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import Tgbot.Bot.Model.crypto;

public class commandsHandler {
    Logger logger = LoggerFactory.getLogger(commandsHandler.class);
    private String cryptoKey;
    private userService userService;


    public void setCryptoKey(String cryptoKey) {
        this.cryptoKey = cryptoKey;
    }

    public void setUserService(Tgbot.Bot.Model.userDAO.userService userService) {
        this.userService = userService;
    }

    public String topCommand() {
        logger.info("/top executed");
        ArrayList<user> allUsers = new ArrayList<>();
        try {
            allUsers = userService.findAll();
            logger.debug("arraylist allUsers filled with: {}", allUsers);
        } catch (Exception e) {
            logger.error("error while filling array with all users: ", e);
        }

        if (allUsers.size() != 0) {
            String topText = "";
            int i = 1;
            for (user allUser : allUsers) {
                if (allUser.getUserLogin() != null) {
                    logger.debug("adding to message user with parameters: {}, {}, {}", allUser.getUserFullName(), allUser.getUserLogin(), allUser.getReputation());
                    topText += i + ". " + allUser.getUserFullName() + " (" + allUser.getUserLogin() + ")" + " с репутацией: " + allUser.getReputation() + "\n";
                } else {
                    logger.debug("adding to message user with parameters: {}, {}", allUser.getUserFullName(), allUser.getReputation());
                    topText += i + ". " + allUser.getUserFullName() + " с репутацией: " + allUser.getReputation() + "\n";

                }
                i++;
            }
            logger.info("/top finished");
            return "топ пользователей по репутации: \n" + topText;
        }
        logger.warn("no users were added to allUsers array!");
        return commandsUtils.UTILnoUsersText;
    }

    public String allCommand() {
        logger.info("/all initiated");
        ArrayList<user> allUsers = new ArrayList<>();

        try {
            logger.debug("trying to fill allUsers array");
            allUsers = userService.findAll();
            logger.debug("array filled with: {}", allUsers);
        } catch (Exception e) {
            logger.error("error while trying to fill array with users: ", e);
        }

        String callAll = "";

        for (user allUser : allUsers) {
            if (allUser.getUserLogin() != null) {
                logger.debug("adding to message user: {}", allUser.getUserLogin());
                callAll += "@" + allUser.getUserLogin() + " ";
            } else {
                logger.debug("adding to message user w/o login: {}, {}", allUser.getId(), allUser.getUserFullName());
                callAll += "<a href=\"tg://user?id=" + allUser.getId() + "\">" + allUser.getUserFullName() + "</a>" + " ";
            }
        }
        logger.info("/all finished");
        return callAll;
    }

    /**
     * modificationType - тип изменения репы
     * 0-понижение
     * 1-увеличение
     **/

    public String changeRepCommand(Message msg, int modificationType) {
        logger.info("changeRepCommand(плюс реп / минус реп) initiated with parameters: {}, {}", msg, modificationType);
        user userToModify;
        user userThatMadeChanges;

        if (msg.getReplyToMessage().getFrom().getIsBot()) {
            logger.debug("there was attempt to change reputation to a BOT");
            return "Нельзя менять репутацию боту.";
        }

        logger.debug("selecting user to modify reputation with id: {}", msg.getReplyToMessage().getFrom().getId());
        userToModify = userService.findById(msg.getReplyToMessage().getFrom().getId());
        logger.debug("found user: {}", userToModify);
        logger.debug("selecting user that modify reputation with id: {}", msg.getFrom().getId());
        userThatMadeChanges = userService.findById(msg.getFrom().getId());
        logger.debug("found user: {}", userThatMadeChanges);

        if (userToModify == null) {
            return "пользователя нет в бд";
        }

        if (userToModify.getId() == userThatMadeChanges.getId()) {
            logger.debug("there was attempt to self rep change");
            return "нельзя изменять реп себе";
        }

        if (modificationType == 1) {
            logger.info("increasing reputation to user: {}", userToModify);
            userToModify.setReputation(userToModify.getReputation() + 1);
            logger.debug("updating user in DB...");
            userService.update(userToModify);
            logger.info("reputation increasing completed");
            return userThatMadeChanges.getUserFullName() + "(" + userThatMadeChanges.getUserLogin() + ")"
                    + " прибавил реп пользователю "
                    + userToModify.getUserFullName() + "(" + userToModify.getUserLogin() + "), текущая репутация: " + userToModify.getReputation();

        } else {
            logger.info("decreasing reputation to user: {}", userToModify);
            userToModify.setReputation(userToModify.getReputation() - 1);
            logger.debug("updating user in DB...");
            userService.update(userToModify);
            logger.info("reputation decreasing completed");
            return userThatMadeChanges.getUserFullName() + "(" + userThatMadeChanges.getUserLogin() + ")"
                    + " понизил реп пользователю "
                    + userToModify.getUserFullName() + "(" + userToModify.getUserLogin() + "), текущая репутация: " + userToModify.getReputation();
        }


        // System.out.println(msg.getReplyToMessage().getFrom().getId()); //916448783

    }


    public String getCourse() throws IOException {
//        https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f
//URL cryptoUrl = new URL("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
//todo сменить криптокомпаре на другое апи
        logger.info("/курс command executed");
        String formatedReturn = "Курсы Крипты: ";
        logger.debug("calling for cryptocompare api ETH...");
        URL requestUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD,rub&api_key=" + cryptoKey);
        String response = httpRequestor.getResponse(requestUrl);
        logger.debug("cryptocompare response: {}", response);

        Gson gson = new Gson();
        logger.debug("deserializing response to ETH obj");
        crypto eth = gson.fromJson(response, crypto.class);
        formatedReturn += "\n ETH:\n" + eth.toString();

        logger.debug("calling for cryptocompare api BTC...");
        requestUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,rub&api_key=" + cryptoKey);
        response = httpRequestor.getResponse(requestUrl);
        logger.debug("cryptocompare response: {}", response);
        logger.debug("deserializing response to BTC obj");
        crypto btc = gson.fromJson(response, crypto.class);
        formatedReturn += "\n BTC:\n" + btc.toString();

        logger.debug("calling for CBR...");
        requestUrl = new URL("https://www.cbr-xml-daily.ru/latest.js");
        response = httpRequestor.getResponse(requestUrl);
        logger.debug("CBR response: {}", response);
        logger.debug("deserializing response to oldMoney obj");
        oldMoney oldMoney = gson.fromJson(response, oldMoney.class);


        formatedReturn += "\nКурсы валют:\n" + " USD: " + String.format("%.2f", 1 / oldMoney.getRates().get("USD"))
                + "\n EUR: " + String.format("%.2f", 1 / oldMoney.getRates().get("EUR"));

        logger.info("/курс finished successfully");
        return formatedReturn;
    }

    public String randomCommand(Message message) {
        logger.info("/rand initiated");
        try {
            String[] split = message.getText().split(" ");
            int limiter;
            try {
                limiter = Integer.parseInt(split[1]);
            } catch (Exception e) {
                limiter = 100;
            }

            logger.debug("limiter = {}", limiter);
            Random rnd = new Random();
            String rndStr = String.valueOf(rnd.nextInt(limiter));
            logger.debug("random value: {}", rnd);

            logger.info("/rand finished");
            return rndStr;
        } catch (Exception e) {
            logger.error("there was a error in /rand", e);
            return "ошибка)";
        }

    }

//    public String getWeather() throws MalformedURLException {
//
//        URL GisMeteo = new URL("");
//
//    }
}