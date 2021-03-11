package Tgbot;

import Tgbot.Bot.bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

// mvn install dependency:copy-dependencies
//        mvn clean heroku:deploy
//        heroku logs -a tg-reputation-bot --tail --force-colors
//        heroku logs -a tg-reputation-bot
//todo google spring properties
@SpringBootApplication
public class main {

    public static void main(String[] args)  {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
       // Tgbot.Bot.bot bot = (bot)context.getBean("bot");
        SpringApplication.run(main.class, args);
    }
}
