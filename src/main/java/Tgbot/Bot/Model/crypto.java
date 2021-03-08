package Tgbot.Bot.Model;

import java.util.HashMap;

public class crypto {
    private double USD;
    private double RUB;

    public crypto(double USD, double RUB) {
        this.USD = USD;
        this.RUB = RUB;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getRUB() {
        return RUB;
    }

    public void setRUB(double RUB) {
        this.RUB = RUB;
    }
    public String toString (){

        return "  RUB: " + getRUB() + "\n  USD: " + getUSD();
    }
}
