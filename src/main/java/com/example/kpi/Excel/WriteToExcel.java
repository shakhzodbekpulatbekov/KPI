package com.example.kpi.Excel;

import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeRepository.TimeRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WriteToExcel {
    final TimeRepository timeRepository;

    public WriteToExcel(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public void writeToFile(String name){

        List<TimeEntity> users = timeRepository.findAll();


        {
            try(FileOutputStream fileOutputStream = new FileOutputStream("/root/kpi/files/Xodimlar Ro'yxati.xls")) {
                HSSFWorkbook xssfWorkbook = new HSSFWorkbook();
                HSSFSheet xssfSheet =xssfWorkbook.createSheet("Xodimlar");



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

                cell=row.createCell(3);
                cell.setCellValue("Sababli");
                cell.setCellStyle(style2);



                for (int i = 0; i < users.size(); i++) {

                    row=xssfSheet.createRow(i+1);
                    cell=row.createCell(0);
                    cell.setCellValue(users.get(i).getDate());
                    cell.setCellStyle(style);

                     cell=row.createCell(1);
                     cell.setCellValue(users.get(i).getTime());


                    Integer integer = Integer.valueOf(users.get(i).getTime().substring(3));
                    String time=users.get(i).getTime();
                if(users.get(i).getUserName().equals("Go'zal")){
                    if ((time.startsWith("08"))){
                        cell.setCellStyle(style2);
                    }else if (time.startsWith("09")&& integer<31){
                        cell.setCellStyle(style1);
                    }
                    else {
                        cell.setCellStyle(style3);
                    }

                    cell=row.createCell(2);
                    cell.setCellValue(users.get(i).getUserName());
                    cell.setCellStyle(style);
                }else if(users.get(i).getUserName().equals("Muzaffar")){
                    if ((time.startsWith("08"))|| time.startsWith("09") || time.startsWith("10")){
                        cell.setCellStyle(style2);

                    }else if (time.startsWith("11")&& integer<16){
                        cell.setCellStyle(style1);
                    }
                    else {cell.setCellStyle(style3);}

                    cell=row.createCell(2);
                    cell.setCellValue(users.get(i).getUserName());
                    cell.setCellStyle(style);
                }else {
                        if ((time.startsWith("08"))){
                        cell.setCellStyle(style2);
                        }else if (time.startsWith("09")&& integer<16){
                        cell.setCellStyle(style1);
                        }
                         else {cell.setCellStyle(style3);}

                             cell=row.createCell(2);
                             cell.setCellValue(users.get(i).getUserName());
                             cell.setCellStyle(style);
                }

                cell=row.createCell(3);
                    String reason = users.get(i).getReason();
                    if (reason==null){
                        cell.setCellValue("-");
                    }else {
                        cell.setCellValue(users.get(i).getReason());
                    }
                    cell.setCellStyle(style);

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
