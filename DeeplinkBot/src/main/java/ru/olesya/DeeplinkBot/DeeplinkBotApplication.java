package ru.olesya.DeeplinkBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.olesya.DeeplinkBot.function.BotConfig;
import ru.olesya.DeeplinkBot.service.TelegramBot;

@SpringBootApplication
public class DeeplinkBotApplication {

	public static void main(String[] args) {
		try{
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(new TelegramBot(new BotConfig()));
		}
		catch(TelegramApiException e){
			e.printStackTrace();
		}
	}

}
