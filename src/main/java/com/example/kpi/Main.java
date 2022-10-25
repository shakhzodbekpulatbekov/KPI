package com.example.kpi;

import com.example.kpi.EmpolymentService.EmploymentService;
import com.example.kpi.Excel.WriteToExcel;
import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeRepository.TimeRepository;
import com.example.kpi.UserEntity.UserEntity;
import com.example.kpi.UserRepository.UserRepository;
import com.example.kpi.UserService.UserService;
import com.example.kpi.buttonController.ButtonController;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
@Component
public class Main extends TelegramLongPollingBot {
    final UserService userService;
    final ButtonController buttonController;
    final UserRepository userRepository;
    final EmploymentService employmentService;
    final TimeRepository timeRepository;
    final KpiRepository kpiRepository;

    public Main(UserService userService, ButtonController buttonController, UserRepository userRepository, EmploymentService employmentService, KpiRepository kpiRepository, TimeRepository timeRepository, KpiRepository kpiRepository1) {
        this.userService = userService;
        this.buttonController = buttonController;
        this.userRepository = userRepository;
        this.employmentService = employmentService;
        this.timeRepository = timeRepository;
        this.kpiRepository = kpiRepository1;
    }

    @Override
    public String getBotUsername() {
        return "t.me/kpi_loretto_bot";
    }

    @Override
    public String getBotToken() {
        return "5750470538:AAGrIoS8FzPvAmOlwV0v9cmMtdYKntarYXg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chat_id=0L;
        String userName;
        String employment="";
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(String.valueOf(915145143));
        sendMessage.setText("command");
        executes(sendMessage);
        if (update.hasMessage()){
            chat_id=update.getMessage().getChatId();
            UserEntity user1 = userService.findUser(chat_id);
            String text = update.getMessage().getText();
            if (user1!=null && user1.getAdminState()==1 && !(text.equals("Xodim qo'shish")) &&
                    !(text.equals("Bugungi kun")) && !(text.equals("Kelgan vaqti")) &&
                    !(text.equals("Excel fayl olish")) && !(text.equals("/start"))){
                employment=update.getMessage().getText();

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                String strDate = formatter.format(date);


                boolean response = employmentService.employmentService(employment, strDate);
                user1.setAdminState(0);
                userRepository.save(user1);
                if (response){
                    sendMessage.setText("Xodim qo'shildi");
                }else {
                    sendMessage.setText("Bu ismli xodim allaqachon mavjud");
                }
                sendMessage.setChatId(String.valueOf(chat_id));
                executes(sendMessage);

            }
           else if (chat_id==652884977 || chat_id==915145143 || chat_id==291105851){
                userName=update.getMessage().getChat().getFirstName();
                text = update.getMessage().getText();
                if (text.equals("/start")){
                    UserEntity user = userService.findUser(chat_id);
                    if (user!=null){
                        ReplyKeyboardMarkup menu = buttonController.menu();
                        executes2(menu,null,"Kerakli bo'limni tanlang!",chat_id);
                    }else {
                        userService.addUser(userName, chat_id, 0);
                        ReplyKeyboardMarkup menu = buttonController.menu();
                        executes2(menu,null,"Kerakli bo'limni tanlang!",chat_id);
                    }
                }else if (text.equals("Xodim qo'shish")){
                    sendMessage.setChatId(String.valueOf(chat_id));
                    sendMessage.setText("Xodim ismini kiriting: ");
                    executes(sendMessage);
                    UserEntity user = userService.findUser(chat_id);
                    if (user==null){
                        UserEntity userEntity = new UserEntity();
                        userEntity.setAdminState(1);
                        userEntity.setChatId(String.valueOf(chat_id));
                        userRepository.save(userEntity);
                    }else {
                        user.setAdminState(1);
                        userRepository.save(user);
                    }
                }else if (text.equals("Xodimni o'chirish")){
                    InlineKeyboardMarkup inlineKeyboardMarkup = buttonController.deleteUser();
                    executes2(null,inlineKeyboardMarkup,"Xodimni tanlang", chat_id);
                }else if (text.equals("Kelgan vaqti")){
                    InlineKeyboardMarkup inlineKeyboardMarkup = userService.userButton();
                    if (inlineKeyboardMarkup==null){
                        sendMessage.setText("Xodimlar ro'yxati bo'sh");
                        sendMessage.setChatId(String.valueOf(chat_id));
                        executes(sendMessage);
                    }else {
                        executes2(null,inlineKeyboardMarkup,"Xodimni tanlang!", chat_id);
                    }

                }else if (text.equals("Bugungi kun")){
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                    String strDate = formatter.format(date);
                    String today = userService.today(strDate);
                    sendMessage=new SendMessage();
                    sendMessage.setChatId(String.valueOf(chat_id));
                    sendMessage.setText(today);
                    executes(sendMessage);
                }else if (text.equals("Excel fayl olish")){
                    WriteToExcel writeToExcel = new WriteToExcel(timeRepository);
                    writeToExcel.writeToFile();
                    String path= "/root/kpi/files/XodimlarRo'yxati.xls";
                    writeToExcel.writeToFile();
                    sendDocument(chat_id, new File(path), "Xodimlar ro'yxati");
                }else if (text.equals("Kelgan vaqtini o'zgartirish")){

                }

                else {
                    sendMessage=new SendMessage();
                    sendMessage.setText("Noto'g'ri buyruq");
                    sendMessage.setChatId(String.valueOf(chat_id));
                    executes(sendMessage);
                }
            }
            else {
                sendMessage.setText("Sizda bu botdan foydalanish huquqi yo'q");
                sendMessage.setChatId(String.valueOf(chat_id));
                executes(sendMessage);
            }
        }
        if (update.hasCallbackQuery()){
            if (update.getCallbackQuery().getData().startsWith("0")){
                String name = update.getCallbackQuery().getData().substring(1);
                timeRepository.deleteByUserName(name);
                sendMessage.setText("OK");
                sendMessage.setChatId(String.valueOf(chat_id));
                executes(sendMessage);
            }else {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                String strDate = formatter.format(date);

                Date date1 = new Date();
                SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
                formatter1.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                String strDate1 = formatter1.format(date1);


                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                String name=update.getCallbackQuery().getData();
                userService.addTime(name,strDate,strDate1);
                sendMessage=new SendMessage();
                sendMessage.setText("OK");

                sendMessage.setChatId(String.valueOf(chatId));
                executes(sendMessage);
            }


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
