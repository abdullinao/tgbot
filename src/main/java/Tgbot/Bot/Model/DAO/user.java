package Tgbot.Bot.Model.DAO;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public")
public class user {
    /*
    CREATE TABLE IF NOT EXISTS  users
    (
        id int,
        name varchar,
        rep int
    );
    */
    @Id
    int id;

    @Column(name = "name")
    String user;

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
