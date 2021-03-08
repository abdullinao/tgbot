package Tgbot.Bot.Handlers;

import Tgbot.APIs.httpWorker;
import Tgbot.Bot.Model.userDAO.user;
import Tgbot.Bot.Model.userDAO.userService;
import Tgbot.Bot.Utils.commandsUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import com.google.gson.Gson;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

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


//            for (int i=0; i<allUsers.size()||i<=5;i++){
//                topText += i+1 + ". " + allUsers.get(i).getUserFullName() +
//                        " (" + allUsers.get(i).getUserLogin() + ")"
//                        + " с репутацией: " + allUsers.get(i).getReputation() + ";\n";
//
//            }


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
            callAll += "@" + allUser.getUserLogin() + " ";
//              if (!allUser.getUserLogin().equals("")) {
//                  callAll += "@" + allUser.getId() + " ";
//              } else {
//                  callAll += "@" + allUser.getUserLogin() + " ";
//              }
        }
        return callAll;
    }

    /**
     * modificationType - тип изменения репы
     * 0-понижение
     * 1-увеличение
     **/

    public String changeRepCommand(Message msg, int modificationType) {


        user userToModify = new user();
        user userThatMadeChanges = new user();


        if (!msg.getReplyToMessage().getFrom().getUserName().equals("KDq3CVM43w_bot"))//todo имя бота должно подтягиваться из конфига
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
            return "Мне нельзя повысить репутацию.";
        }

        // System.out.println(msg.getReplyToMessage().getFrom().getId()); //916448783

    }


    public String getCourse() throws IOException {
//        https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f
//URL cryptoUrl = new URL("https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,RUB&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
//todo сменить криптокомпаре на другое апи
        //todo вынести ключ в конфиг файл
        String formatedReturn = "Курсы валют: ";
        URL cryptoUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD,rub&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
        String response = httpWorker.getResponse(cryptoUrl);
        Gson gson = new Gson();
        crypto eth = gson.fromJson(response, crypto.class);
        formatedReturn += "\nETH:\n" + eth.toString();

        cryptoUrl = new URL("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,rub&api_key=1437b60bf3857dbcd682b5049a2b1c4fe31956b03bebcd28587086c77ec8870f");
        response = httpWorker.getResponse(cryptoUrl);
        crypto btc = gson.fromJson(response, crypto.class);
        formatedReturn += "\nBTC:\n" + btc.toString();


        return formatedReturn;
//
//         Type collectionType = new TypeToken<Collection<crypto>>(){}.getType();
//         Collection<crypto> cryptos = gson.fromJson(response, collectionType);
//         System.out.println(cryptos.size());
//
//         for (crypto crypto : cryptos) {
//             System.out.println(crypto);
//             System.out.println("---");
//
//         }

        //  return response;

    }

//    public String getWeather() throws MalformedURLException {
//
//        URL GisMeteo = new URL("");
//
//    }
}