package Tgbot.Bot.Model.DAO;

import java.util.ArrayList;

public class userService {


    private userDAO userDAO = new userDAO();


    public userService(userDAO userDAO) {
        this.userDAO = userDAO;
    }

    public userService() {

    }

    public void update(user user) {
        userDAO.openCurrentSessionwithTransaction();
        userDAO.update(user);
        userDAO.closeCurrentSessionwithTransaction();
    }

    public user findById(int id) {
        userDAO.openCurrentSession();
        user user = userDAO.findById(id);
        userDAO.closeCurrentSession();
        return user;
    }

//    public void delete(int id) {
//        fdaoc.openCurrentSessionwithTransaction();
//        figureUltraCrutch fuc = bookDao.findById(id);
//        fdaoc.delete(book);
//        fdaoc.closeCurrentSessionwithTransaction();
//    }

    public ArrayList<user> findAll() {
        userDAO.openCurrentSession();
        ArrayList<user> users = userDAO.findAll();
        userDAO.closeCurrentSession();
        return users;
    }


}
