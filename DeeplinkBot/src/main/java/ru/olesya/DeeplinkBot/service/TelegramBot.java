package ru.olesya.DeeplinkBot.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.olesya.DeeplinkBot.function.BotConfig;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    private String mp;
    private String id;
    private String lastMessageBot;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {//предоставляем DPI ключ
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/help")) {
                    sendMessage(message.getChatId(), """
                            \s
                            /createdeeplink Создать диплинк
                            /getall Посмотреть свои диплинки
                            /statistics Статистика диплинков
                            /start приветствие
                            /help помощь""");

                }else if (text.equals("/start")) {
                    sendMessage(message.getChatId(), """
                            Привет! Этот бот умеет создавать Диплинк для товара из таких маркетплэйсов как WB и Ozon.\s
                            Создать диплинк /createdeeplink""");

                } else if (Objects.equals(text, "/createdeeplink")) {
                    lastMessageBot = "mp?";
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(lastMessageBot, "mp")) {
                    lastMessageBot = "link";
                    String id = getIdToLink(text, mp);
                    sendMessage(message.getChatId(), "Ваш диплинк:" +
                            "\nhttp://api:8080/dp/deeplink/" + mp + "/" + id);
                    sendMessage(message.getChatId(), "Хотите сделать ещё один диплинк?\nНажмите /createdeeplink\nВсе функции: /help");
                    lastMessageBot = "";
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost("http://api:8080/dp/create/" + message.getChatId() + "/" + mp + "/" + id);
                    CloseableHttpResponse response = null;
                    try {
                        response = httpclient.execute(httpPost);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (text.equals("/getall")) {
                    String url = "http://api:8080/dp/getDps/" + message.getChatId();
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet(url);
                    String responseString = null;
                    try {
                        sendMessage(message.getChatId(), "Ваши диплинки:\n" + EntityUtils.toString(httpClient.execute(httpGet).getEntity()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (text.equals("/statistics")) {
                    String url = "http://api:8080/dp/statistics/" + message.getChatId();
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet(url);
                    String responseString = null;
                    try {
                        sendMessage(message.getChatId(), EntityUtils.toString(httpClient.execute(httpGet).getEntity()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sendMessage(message.getChatId(), "Для начала работы с ботом нажмите /start");
                }
            }
        } else if (update.hasCallbackQuery()&&lastMessageBot.equals("mp?")) {
            lastMessageBot = "mp";
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(
                    update.getCallbackQuery().getData());
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
            if(sendMessage.getText().equals("Введите id товара или ссылку!")){
                mp = "wb";
            }else{
                mp="ozon";
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendMessage(long chatid, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatid));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public static SendMessage sendInlineKeyBoardMessage(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Wildberries");
        inlineKeyboardButton1.setCallbackData("Введите id товара или ссылку!");
        inlineKeyboardButton2.setText("Ozon");
        inlineKeyboardButton2.setCallbackData("Введите id товара или ссылку");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sendMessage = new SendMessage(chatId.toString(), "Какой маркетплэйс у твоего товара?");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    public String getIdToLink(String link, String mp){
        String[] res =  link.split("/");
        if(res.length==1){
            return link;
        }
        if (mp.equals("wb")){
            return res[4];
        }else if (mp.equals("ozon")){
            res = res[4].split("-");
            return res[res.length-1];
        }
        return null;
    }
}
