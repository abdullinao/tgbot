package Tgbot.Bot.Model.DAO;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
public class user {
    /*
  create table users
(
    id   integer not null,
    name varchar,
    rep  integer
);
    */
    @Id
    int id;

    @Column(name = "name")
    String userFullName;

    @Column(name = "login")
    String userLogin;

    @Column(name = "rep")
    int reputation;

    public user() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String user) {
        this.userFullName = user;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
