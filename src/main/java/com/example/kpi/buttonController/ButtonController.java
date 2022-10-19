package com.example.kpi.buttonController;

import com.example.kpi.KpiEntity.KpiEntity;
import com.example.kpi.KpiRepository.KpiRepository;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Controller
public class ButtonController {
    final KpiRepository kpiRepository;

    public ButtonController(KpiRepository kpiRepository) {
        this.kpiRepository = kpiRepository;
    }

    public ReplyKeyboardMarkup menu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setResizeKeyboard(true);
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow.add("Kelgan vaqti");
        keyboardRow1.add("Bugungi kun");
        keyboardRow1.add("Xodim qo'shish");
        keyboardRow2.add("Excel fayl olish");
        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    public InlineKeyboardMarkup deleteUser() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        List<KpiEntity> all = kpiRepository.findAll();
        int helper = 0;

        if (!all.isEmpty()) {
            for (int i = 0; i < all.size(); i++) {
                inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(all.get(i).getUserName());
                inlineKeyboardButton.setCallbackData("0"+all.get(i).getUserName());
                inlineKeyboardButtons.add(inlineKeyboardButton);
                list.add(inlineKeyboardButtons);
                inlineKeyboardButtons = new ArrayList<>();

            }

        }
        return inlineKeyboardMarkup;
    }
}
