package ru.olesya.DeeplinkBot.function;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    final String botName = "DeeplinkMarketBot";
    final String token = "6382177329:AAFg2wKvOfDNDXyeK4p8z2vPcXB3rtlzyOo";

    public String getBotName() {
        return botName;
    }
    public String getToken() {
        return token;
    }
}
