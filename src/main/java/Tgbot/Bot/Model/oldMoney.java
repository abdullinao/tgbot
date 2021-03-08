package Tgbot.Bot.Model;

import java.util.Map;

public class oldMoney {

//"disclaimer": "https://www.cbr-xml-daily.ru/#terms",
//        "date": "2021-03-06",
//        "timestamp": 1614988800,
//        "base": "RUB",
//        "rates": {

    private String disclaimer;
    private String date;
    private String base;
    private Map<String, Double> rates;

    public oldMoney(String disclaimer, String date, String base, Map<String, Double> rates) {
        this.disclaimer = disclaimer;
        this.date = date;
        this.base = base;
        this.rates = rates;
    }


    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
