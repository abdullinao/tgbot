package Tgbot.Bot.Handlers;

import Tgbot.externalAPIs.httpRequestor;
import Tgbot.Bot.Model.oldMoney;
import Tgbot.Bot.Model.userDAO.user;
import Tgbot.Bot.Model.userDAO.userService;
import Tgbot.Bot.Utils.commandsUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import Tgbot.Bot.Model.crypto;

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
                topText += i + ". " + allUser.getUserFullName() + " (" + allUser.getUserLogin() + ")" + " с репутацией: " + allUser.getReputation() + ";\n";
                i++;
            }
            return "топ пользователей по репутации: \n" + topText;
        }

        return commandsUtils.UTILnoUsersText;
    }

    public String allCommand() {
        ArrayList<user> allUsers = new ArrayList<>();

        try {
            allUsers = userService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        String callAll = "";

        for (user allUser : allUsers) {
            if (allUser.getUserLogin()!=null) {
                callAll += "@" + allUser.getUserLogin() + " ";
            }
        }
        return callAll;
    }

    /**
     * modificationType - тип изменения репы
     * 0-понижение
     * 1-увеличение
     **/

    public String changeRepCommand(Message msg, int modificationType) {


        user userToModify;
        user userThatMadeChanges;

        if (!msg.getReplyToMessage().getFrom().getIsBot())
        {
            userToModify = userService.findById(msg.getReplyToMessage().getFrom().getId());
            userThatMadeChanges = userService.findById(msg.getFrom().getId());

            if (userToModify.getId() == userThatMadeChanges.getId()) {
                return "нельзя изменять реп себе";
            }

            if (modificationType == 1) {
                userToModify.setReputation(userToModify.getReputation() + 1);

                userService.update(userToModify);
                return userThatMadeChanges.getUserFullName() + "(" + userThatMadeChanges.getUserLogin() + ")"
                        + " прибавил реп пользователю "
                        + userToModify.getUserFullName() + "(" + userToModify.getUserLogin() + ")";

            } else {
                userToModify.setReputation(userToModify.getReputation() - 1);
                userService.update(userToModify);
                return userThatMadeChanges.getUserFullName() + "(" + userThatMadeChanges.getUserLogin() + ")"
                        + " понизил реп пользователю "
                        + userToModify.getUserFullName() + "(" + userToModify.getUserLogin() + ")";
            }


        } else {
            return "Нельзя менять репутацию боту.";
        }

        // System.out.println(msg.getReplyToMessage().getFrom().getId()); //916448783

    }


    public String getCourse() throws IOException {
//        https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f
//URL cryptoUrl = new URL("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
//todo сменить криптокомпаре на другое апи
        //todo вынести ключ в конфиг файл

        String formatedReturn = "Курсы Крипты: ";
        URL requestUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD,rub&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
        String response = httpRequestor.getResponse(requestUrl);
        Gson gson = new Gson();
        crypto eth = gson.fromJson(response, crypto.class);
        formatedReturn += "\n ETH:\n" + eth.toString();

        requestUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,rub&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
        response = httpRequestor.getResponse(requestUrl);
        crypto btc = gson.fromJson(response, crypto.class);
        formatedReturn += "\n BTC:\n" + btc.toString();


        requestUrl = new URL("https://www.cbr-xml-daily.ru/latest.js");
        response = httpRequestor.getResponse(requestUrl);
        oldMoney oldMoney = gson.fromJson(response, oldMoney.class);


        formatedReturn += "\nКурсы валют:\n" + " USD: " + String.format("%.2f", 1 / oldMoney.getRates().get("USD"))
                + "\n EUR: " + String.format("%.2f", 1 / oldMoney.getRates().get("EUR"));


        return formatedReturn;
     }

//    public String getWeather() throws MalformedURLException {
//
//        URL GisMeteo = new URL("");
//
//    }
}