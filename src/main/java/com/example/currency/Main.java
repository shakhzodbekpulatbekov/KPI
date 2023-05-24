package com.example.currency;

import com.example.currency.CurrencyApiDto.CurrencyApiDto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class Main extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "t.me/Loretto_currency_bot";
    }

    @Override
    public String getBotToken() {
        return "6061392489:AAEpa8zYly6QwXuEgLeZLrlK_M0ZMfEpy88";
    }

    @Override
    public void onUpdateReceived(Update update) {

        Long chat_id=0L;
        String userName;
        String employment="";
        SendMessage sendMessage=new SendMessage();
        if (update.hasMessage()) {

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
            String strDate = formatter.format(date);


            chat_id=update.getMessage().getChatId();
            sendMessage= new SendMessage();
            sendMessage.setChatId(String.valueOf(chat_id));
            String rate= getCurrencyRate();
            String rate1=rate.substring(0,5);
            Integer rateInt= Integer.parseInt(rate1)-20;
            sendMessage.setText("Dollar kursi \uD83C\uDDFA\uD83C\uDDF8 \n"+String.valueOf(rateInt)+" \n"+strDate+" "+"holatiga");
            executes(sendMessage);



        }
    }
    public static CurrencyApiDto getCurrency() {
        try {

            RestTemplate restTemplate = new RestTemplate();
            String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";

            //API DAN MA'LUMOTLAR OLINDI
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            //STRINGNI DTOGA O'RAB DOLLARNI AJRATIB OLDIK JAMI VALYUTALAR ORASIDAN
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            List<CurrencyApiDto> res = mapper.readValue(response.getBody(), new TypeReference<List<CurrencyApiDto>>() {
            }).stream().filter(c -> c.getCode().equals("840") && c.getCcy().equals("USD")).collect(Collectors.toList());

            //DOLLARNI RATE NI QAYTARYAPMIZ
            return res.get(0);
//             System.out.println(res.get(0).getRate());


        } catch (Exception e) {

            //AGAR APIDAN MA'LUMOT KELMASA 0 QAYTARAMIZ
            return new CurrencyApiDto();
//             System.out.println(0);
        }
    }

    public static String getCurrencyRate() {
        try {
            String dollar = "";
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://cbu.uz/uz/arkhiv-kursov-valyut/json/";

            //API DAN MA'LUMOTLAR OLINDI
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            //STRINGNI DTOGA O'RAB DOLLARNI AJRATIB OLDIK JAMI VALYUTALAR ORASIDAN
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            List<CurrencyApiDto> res = mapper.readValue(response.getBody(), new TypeReference<List<CurrencyApiDto>>() {
            }).stream().filter(c -> c.getCode().equals("840") && c.getCcy().equals("USD")).collect(Collectors.toList());
            dollar = res.get(0).getRate();
            return dollar;

        } catch (Exception e) {

            //AGAR APIDAN MA'LUMOT KELMASA 0 QAYTARAMIZ
//             System.out.println(0);
            return "0";
        }
    }
    public void executes(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void executes2
            (
                    ReplyKeyboardMarkup replyKeyboardMarkup,
                    InlineKeyboardMarkup inlineKeyboardMarkup,
                    String text, Long chatId
            ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(String.valueOf(chatId));
        if (replyKeyboardMarkup != null)
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
//            id = sendMessage.getReplyToMessageId();
        if (inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendDocument(Long chatId, File save, String caption){

        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(String.valueOf(chatId));
        sendDocumentRequest.setDocument(new InputFile(save));
        sendDocumentRequest.setCaption(caption);
        executeDocument(sendDocumentRequest);
    }
    public void executeDocument(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
