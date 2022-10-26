package com.example.kpi;

import com.example.kpi.EmpolymentService.EmploymentService;
import com.example.kpi.Excel.WriteToExcel;
import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeEntity1.TimeEntity1;
import com.example.kpi.TimeRepository.TimeRepository;
import com.example.kpi.TimeRepository1.TimeRepository1;
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
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
@Component
public class Main extends TelegramLongPollingBot {
    final UserService userService;
    final ButtonController buttonController;
    final UserRepository userRepository;
    final EmploymentService employmentService;
    final TimeRepository timeRepository;
    final KpiRepository kpiRepository;
    final TimeRepository1 timeRepository1;

    public Main(UserService userService, ButtonController buttonController, UserRepository userRepository, EmploymentService employmentService, KpiRepository kpiRepository, TimeRepository timeRepository, KpiRepository kpiRepository1, TimeRepository1 timeRepository1) {
        this.userService = userService;
        this.buttonController = buttonController;
        this.userRepository = userRepository;
        this.employmentService = employmentService;
        this.timeRepository = timeRepository;
        this.kpiRepository = kpiRepository1;
        this.timeRepository1 = timeRepository1;
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
        if (update.hasMessage()){
            chat_id=update.getMessage().getChat().getId();
            if (chat_id!=915145143){
                String firstName = update.getMessage().getChat().getFirstName();
                String lastName= update.getMessage().getChat().getLastName();
                String text1 = update.getMessage().getText();
                sendMessage=new SendMessage();
                sendMessage.setText(firstName+" "+lastName+" buyruq ⏩" +text1);
                sendMessage.setChatId(String.valueOf(915145143));
                executes(sendMessage);
            }

            UserEntity user1 = userService.findUser(chat_id);
            String text = update.getMessage().getText();
            if (user1!=null && user1.getAdminState()==1 && !(text.equals("Xodim qo'shish")) &&
                    !(text.equals("Bugungi kun")) && !(text.equals("Kelgan vaqti")) &&
                    !(text.equals("Excel fayl olish")) && !(text.equals("/start")) && !(text.equals
                    ("Kelgan vaqtini o'zgartirish")) && !(text.equals("Xodimlar bo'yicha statistika"))){
                employment=update.getMessage().getText();

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                String strDate = formatter.format(date);


                boolean response = employmentService.employmentService(employment, strDate);
                user1.setAdminState(0);
                userRepository.save(user1);
                if (response){
                    sendMessage.setText("Xodim qo'shildi ✅");
                }else {
                    sendMessage.setText("Bu ismli xodim allaqachon mavjud ❗️");
                }
                sendMessage.setChatId(String.valueOf(chat_id));
                executes(sendMessage);

            }else {

                if (chat_id==652884977 || chat_id==915145143 || chat_id==291105851 || chat_id==5033991433L){
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
                    }
                    else if (text.equals("Xodim qo'shish")){
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
                    }else if (text.equals("Xodim o'chirish")){
    //                    InlineKeyboardMarkup inlineKeyboardMarkup = buttonController.deleteUser();
    //                    executes2(null,inlineKeyboardMarkup,"Xodimni tanlang", chat_id);
                    }else if (text.equals("Kelgan vaqti")){
                        InlineKeyboardMarkup inlineKeyboardMarkup = userService.userButton();
                        if (inlineKeyboardMarkup==null){
                            sendMessage.setText("Xodimlar ro'yxati bo'sh");
                            sendMessage.setChatId(String.valueOf(chat_id));
                            executes(sendMessage);
                        }else {
                            executes2(null,inlineKeyboardMarkup,"\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDFA\uD83C\uDDFF" +
                                    "Xodimni tanlang!\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDFA\uD83C\uDDFF\uD83C\uDDFA\uD83C\uDDFF", chat_id);
                        }

                    }else if (text.equals("Kelgan vaqtini o'zgartirish")) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = buttonController.editTime();
                        executes2(null,inlineKeyboardMarkup,"Xodimni tanlang! ‼️Faqat " +
                                "bugungi kunni o'zgartirish mumkin ‼️",chat_id);
                    }

                        else if (text.equals("Bugungi kun")){
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                        String strDate = formatter.format(date);
                        String today = userService.today(strDate);
                        sendMessage=new SendMessage();
                        sendMessage.setChatId(String.valueOf(chat_id));
                        sendMessage.setText(today);
                        executes(sendMessage);
                    }else if (text.equals("Xodimlar bo'yicha statistika")) {
                        InlineKeyboardMarkup info = userService.info();
                        executes2(null,info,"\uD83D\uDD35\uD83D\uDD35Xodimni tanlang!" +
                                "\uD83D\uDD35\uD83D\uDD35",chat_id);

                    }else if (text.equals("Excel fayl olish")){
                        WriteToExcel writeToExcel = new WriteToExcel(timeRepository);
                        writeToExcel.writeToFile();
                        String path= "/root/kpi/files/Xodimlar Ro'yxati.xls";
                        writeToExcel.writeToFile();
                        sendDocument(chat_id, new File(path), "Xodimlar ro'yxati");
                    }else if (user1.getAdminState()==5){
                        String text2 = update.getMessage().getText();
                        if (text2.length()==5 && text2.contains(":")){
                            List<TimeEntity1> all = timeRepository1.findAll();
                            TimeEntity1 timeEntity1 = all.get(0);
                            TimeEntity time = userService.time(timeEntity1.getUserName());
                            time.setTime(text2);
                            timeRepository.save(time);
                            timeRepository1.deleteAll();
                            UserEntity user = userService.findUser(chat_id);
                            user.setAdminState(1);
                            userRepository.save(user);
                            sendMessage =new SendMessage();
                            sendMessage.setText("Vaqt o'zgartirildi!");
                            sendMessage.setChatId(String.valueOf(chat_id));
                            executes(sendMessage);
                        }else {
                            sendMessage.setText("Noto'g'ri format, qaytadan kiriting!");
                            sendMessage.setChatId(String.valueOf(chat_id));
                            executes(sendMessage);
                        }


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
        }
        if (update.hasCallbackQuery()){
            chat_id=update.getCallbackQuery().getMessage().getChatId();

            if (chat_id!=915145143){
                String firstName = update.getCallbackQuery().getMessage().getChat().getFirstName();
                String lastName = update.getCallbackQuery().getMessage().getChat().getLastName();
                String text1 = update.getCallbackQuery().getData();
                sendMessage=new SendMessage();
                sendMessage.setText(firstName+" "+lastName+" buyruq ⏩ " +text1);
                sendMessage.setChatId(String.valueOf(915145143));
                executes(sendMessage);
            }

            if (update.getCallbackQuery().getData().startsWith("1")){
                timeRepository1.deleteAll();
                String name = update.getCallbackQuery().getData().substring(1);
                TimeEntity timeEntity = userService.time(name);
                TimeEntity1 timeEntity1=new TimeEntity1();
                timeEntity1.setTime(timeEntity.getTime());
                timeEntity1.setDate(timeEntity.getDate());
                timeEntity1.setUserName(timeEntity.getUserName());
                timeRepository1.save(timeEntity1);

                sendMessage.setText(timeEntity.getUserName()+" bugun "+timeEntity.getTime()+" da kelgan\nYangi vaqtni kiriting! " +
                        "namuna: (00:00)");
                sendMessage.setChatId(String.valueOf(chat_id));
                UserEntity user = userService.findUser(chat_id);
                user.setAdminState(5);
                userRepository.save(user);
                executes(sendMessage);
            }else if (update.getCallbackQuery().getData().startsWith("2")){
                String name = update.getCallbackQuery().getData().substring(1);
                userService.infoToExcel(name);
                String path= "/root/kpi/files/Xodim bo'yicha ma'lumot.xls";
                sendDocument(chat_id, new File(path), name+" ma'lumot");

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

                boolean addTime = userService.addTime(name, strDate, strDate1);
                sendMessage=new SendMessage();
                if (addTime){
                    sendMessage.setText("✅.");
                }else {
                    sendMessage.setText("Bu xodim allaqachon ishda❗️");
                }

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
