package Tgbot.Bot.Model.userDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;

public class userDAO {


    private static SessionFactory sessionFactory;

    private Session currentSession;

    private Transaction currentTransaction;

    public userDAO() {
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(user.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void update(user user) {
        getCurrentSession().update(user);
    }

    public user findById(int id) {
        user user = getCurrentSession().get(user.class, id);
        return user;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<user> findAll() {
        return (ArrayList<user>) getCurrentSession()
                .createQuery("from user ORDER BY reputation desc").list(); //вывод всех пользователей с
        //сортировкой по убыванию репы
    }

    public void persist(user user) {
        getCurrentSession().save(user);
    }

}
