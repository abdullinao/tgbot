package Tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// mvn install dependency:copy-dependencies
//        mvn clean heroku:deploy
//        heroku logs -a tg-reputation-bot --tail --force-colors
//        heroku logs -a tg-reputation-bot
//todo google spring properties
@SpringBootApplication
public class main {
    public static void main(String[] args)  {
        SpringApplication.run(main.class, args);
    }
}
