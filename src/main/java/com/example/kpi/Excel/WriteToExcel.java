package com.example.kpi.Excel;

import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeRepository.TimeRepository;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteToExcel {
    final TimeRepository timeRepository;

    public WriteToExcel(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public void writeToFile(){
        List<TimeEntity> users = timeRepository.findAll();

        {
            try(FileOutputStream fileOutputStream = new FileOutputStream("Touch resources/XodimlarRo'yxati.xls")) {
                HSSFWorkbook xssfWorkbook = new HSSFWorkbook();
                HSSFSheet xssfSheet =xssfWorkbook.createSheet("Xodimlar");
                HSSFRow row=xssfSheet.createRow(0);
                row.createCell(0).setCellValue("Kun");
                row.createCell(1).setCellValue("Soat");
                row.createCell(2).setCellValue("Ism");


                for (int i = 0; i < users.size(); i++) {
                    row=xssfSheet.createRow(i+1);
                    row.createCell(0).setCellValue(users.get(i).getDate());
                    row.createCell(1).setCellValue(users.get(i).getTime());
                    row.createCell(2).setCellValue(users.get(i).getUserName());
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
