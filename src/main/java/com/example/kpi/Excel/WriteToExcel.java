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
        List<TimeEntity> users;


        if (name == null) {
            users = timeRepository.findAllSorted();
        } else {
            users = timeRepository.findByUserName(name);
        }


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

//                Font font = xssfWorkbook.createFont();
//                font.setBold(true);
////                font.setUnderline(Font.U_SINGLE);
//                font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
//                style1.setFont(font);


                HSSFRow row=xssfSheet.createRow(0);
                HSSFCell cell=row.createCell(0);
                cell.setCellValue("KUN");
                cell.setCellStyle(style);


                cell=row.createCell(1);
                cell.setCellValue("SOAT");
                cell.setCellStyle(style);


                cell=row.createCell(2);
                cell.setCellValue("ISM");
                cell.setCellStyle(style);


                cell=row.createCell(3);
                cell.setCellValue("Sababli");
                cell.setCellStyle(style);




                for (int i = 0; i < users.size(); i++) {

                    row=xssfSheet.createRow(i+1);
                    cell=row.createCell(0);
                    cell.setCellValue(users.get(i).getDate());
                    cell.setCellStyle(style);

                    cell=row.createCell(1);
                    cell.setCellValue(users.get(i).getTime());
                    cell.setCellStyle(style);

                    cell=row.createCell(2);
                    cell.setCellValue(users.get(i).getUserName());
                    cell.setCellStyle(style);

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