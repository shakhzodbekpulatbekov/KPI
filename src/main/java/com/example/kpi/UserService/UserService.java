package com.example.kpi.UserService;

import com.example.kpi.KpiEntity.KpiEntity;
import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeRepository.TimeRepository;
import com.example.kpi.UserEntity.UserEntity;
import com.example.kpi.UserRepository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final UserRepository userRepository;
    final KpiRepository kpiRepository;
    final TimeRepository timeRepository;

    public UserService(UserRepository userRepository, KpiRepository kpiRepository, TimeRepository timeRepository) {
        this.userRepository = userRepository;
        this.kpiRepository = kpiRepository;
        this.timeRepository = timeRepository;
    }

    public void addUser(String userName, Long chatId, int state) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setChatId(String.valueOf(chatId));
        userRepository.save(userEntity);
    }

    public UserEntity findUser(Long chatId) {
        Optional<UserEntity> byChatId = userRepository.findByChatId(String.valueOf(chatId));
        if (byChatId.isPresent()) {
            UserEntity userEntity = byChatId.get();
            return userEntity;
        } else return null;

    }

    public InlineKeyboardMarkup userButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        List<KpiEntity> all = kpiRepository.findAll();
        int helper = 0;

        if (!all.isEmpty()) {
            for (int i = 0; i < all.size(); i++) {
                helper++;

                inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(all.get(i).getUserName());
                inlineKeyboardButton.setCallbackData(all.get(i).getUserName());
                inlineKeyboardButtons.add(inlineKeyboardButton);
//                list.add(inlineKeyboardButtons);
//                inlineKeyboardButtons = new ArrayList<>();

                if (helper%2==0){
                    list.add(inlineKeyboardButtons);
                    inlineKeyboardButtons= new ArrayList<>();
                }if (helper==all.size() && helper%2!=0){
                    list.add(inlineKeyboardButtons);
                    inlineKeyboardButtons=new ArrayList<>();
                }
            }

        }
        return inlineKeyboardMarkup;
    }

    public void addTime(String name, String date, String time){
        TimeEntity timeEntity = new TimeEntity();
        timeEntity.setTime(time);
        timeEntity.setUserName(name);
        timeEntity.setDate(date);
        timeRepository.save(timeEntity);
    }

    public String repos(){
        List<TimeEntity> all = timeRepository.findAll();
        String response = all.toString();
        return response;
    }

    public String today(String date){
        String response="";
        List<TimeEntity> byDate = timeRepository.findByDate(date);
        if (!byDate.isEmpty()){
            for (int i = 0; i < byDate.size(); i++) {
                response+=byDate.get(i).getDate()+"\t\t"+byDate.get(i).getTime()+"\t\t"+byDate.get(i).getUserName()+"\n";
            }
        }
        return response;
    }

}

