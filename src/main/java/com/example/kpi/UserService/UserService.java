package com.example.kpi.UserService;

import com.example.kpi.KpiEntity.KpiEntity;
import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeRepository.TimeRepository;
import com.example.kpi.UserEntity.UserEntity;
import com.example.kpi.UserRepository.UserRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public boolean addTime(String name, String date, String time){
        boolean isAdded=true;
        List<TimeEntity> all = timeRepository.findAll();
        for (TimeEntity entity : all) {
            if (entity.getUserName().equals(name)) {
                if (entity.getDate().equals(date)) {
                    isAdded = false;
                }
            }
        }
        if (isAdded){
            TimeEntity timeEntity = new TimeEntity();
            timeEntity.setTime(time);
            timeEntity.setUserName(name);
            timeEntity.setDate(date);
            timeRepository.save(timeEntity);

        }
        return isAdded;

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

    public TimeEntity time(String name){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
        String strDate = formatter.format(date);
        TimeEntity timeEntity= new TimeEntity();
        List<TimeEntity> byUserName = timeRepository.findByUserName(name);
        for (int i = 0; i < byUserName.size(); i++) {
            if (byUserName.get(i).getUserName().equals(name)){
                if (byUserName.get(i).getDate().equals(strDate)){
                    timeEntity=byUserName.get(i);
                }
            }
        }
        return timeEntity;

    }
    public InlineKeyboardMarkup info() {
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
                inlineKeyboardButton.setCallbackData(2+all.get(i).getUserName());
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

    public void infoToExcel(String name){
        List<TimeEntity> byUserName = timeRepository.findByUserName(name);
        {
            try(FileOutputStream fileOutputStream = new FileOutputStream("/root/kpi/files/Xodim bo'yicha ma'lumot.xls")) {
                HSSFWorkbook xssfWorkbook = new HSSFWorkbook();
                HSSFSheet xssfSheet =xssfWorkbook.createSheet("Xodim");


                CellStyle style=xssfWorkbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderTop(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderLeft(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style.setBorderRight(BorderStyle.THIN);
                style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

                CellStyle style1=xssfWorkbook.createCellStyle();
                style1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
                style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//                Font font = xssfWorkbook.createFont();
//                font.setBold(true);
////                font.setUnderline(Font.U_SINGLE);
//                font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
//                style1.setFont(font);

                CellStyle style2=xssfWorkbook.createCellStyle();
                style2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
                style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style2.setBorderBottom(BorderStyle.THIN);
                style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style2.setBorderTop(BorderStyle.THIN);
                style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style2.setBorderLeft(BorderStyle.THIN);
                style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style2.setBorderRight(BorderStyle.THIN);
                style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                Font font1 = xssfWorkbook.createFont();
                font1.setBold(true);
//                font.setUnderline(Font.U_SINGLE);
                font1.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                style2.setFont(font1);

                CellStyle style3=xssfWorkbook.createCellStyle();
                style3.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style3.setBorderBottom(BorderStyle.THIN);
                style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style3.setBorderTop(BorderStyle.THIN);
                style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style3.setBorderLeft(BorderStyle.THIN);
                style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                style3.setBorderRight(BorderStyle.THIN);
                style3.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                Font font2 = xssfWorkbook.createFont();
                font2.setBold(true);
//                font.setUnderline(Font.U_SINGLE);
                font2.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                style3.setFont(font2);

                HSSFRow row=xssfSheet.createRow(0);
                HSSFCell cell=row.createCell(0);
                cell.setCellValue("KUN");
                cell.setCellStyle(style2);

                cell=row.createCell(1);
                cell.setCellValue("SOAT");
                cell.setCellStyle(style2);

                cell=row.createCell(2);
                cell.setCellValue("ISM");
                cell.setCellStyle(style2);



                for (int i = 0; i < byUserName.size(); i++) {
                    row=xssfSheet.createRow(i+1);
                    cell=row.createCell(0);
                    cell.setCellValue(byUserName.get(i).getDate());
                    cell.setCellStyle(style);

                    cell=row.createCell(1);
                    cell.setCellValue(byUserName.get(i).getTime());

                    Integer integer = Integer.valueOf(byUserName.get(i).getTime().substring(3));
                    String time=byUserName.get(i).getTime();
                    if(byUserName.get(i).getUserName().equals("Go'zal")){
                        if ((time.startsWith("08"))){
                            cell.setCellStyle(style2);
                        }else if (time.startsWith("09")&& integer<31){
                            cell.setCellStyle(style1);
                        }
                        else cell.setCellStyle(style3);

                        cell=row.createCell(2);
                        cell.setCellValue(byUserName.get(i).getUserName());
                        cell.setCellStyle(style);
                    }else if(byUserName.get(i).getUserName().equals("Muzaffar")){
                        if ((time.startsWith("08"))|| time.startsWith("09") || time.startsWith("10")){
                            cell.setCellStyle(style2);
                        }else if (time.startsWith("11")&& integer<16){
                            cell.setCellStyle(style1);
                        }
                        else cell.setCellStyle(style3);

                        cell=row.createCell(2);
                        cell.setCellValue(byUserName.get(i).getUserName());
                        cell.setCellStyle(style);
                    }else {
                        if ((time.startsWith("08"))){
                            cell.setCellStyle(style2);
                        }else if (time.startsWith("09")&& integer<16){
                            cell.setCellStyle(style1);
                        }
                        else cell.setCellStyle(style3);

                        cell=row.createCell(2);
                        cell.setCellValue(byUserName.get(i).getUserName());
                        cell.setCellStyle(style);
                    }
                }
                xssfWorkbook.write(fileOutputStream);
                xssfWorkbook.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

