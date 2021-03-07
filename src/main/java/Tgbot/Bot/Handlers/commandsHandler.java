package Tgbot.Bot.Handlers;

import Tgbot.Bot.Model.DAO.user;
import Tgbot.Bot.Model.DAO.userService;
import Tgbot.Bot.Utils.commandsUtils;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

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

            if (userToModify.getId()==userThatMadeChanges.getId()){
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
            return  "Мне нельзя повысить репутацию.";
        }

        // System.out.println(msg.getReplyToMessage().getFrom().getId()); //916448783

    }


}