package Tgbot.Bot;

import Tgbot.Bot.Model.DAO.user;
import Tgbot.Bot.Model.DAO.userService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    public void plusRepCommand(Message msg){
        System.out.println(msg.getFrom());

        System.out.println(msg.getForwardFrom());
        System.out.println(msg);
        System.out.println(msg);
    }

}