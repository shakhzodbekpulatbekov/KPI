package com.example.kpi.Excel;

import com.example.kpi.KpiRepository.KpiRepository;
import com.example.kpi.TimeEntity.TimeEntity;
import com.example.kpi.TimeRepository.TimeRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

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
            try(FileOutputStream fileOutputStream = new FileOutputStream("/root/kpi/files/Xodimlar Ro'yxati.xls")) {
                HSSFWorkbook xssfWorkbook = new HSSFWorkbook();
                HSSFSheet xssfSheet =xssfWorkbook.createSheet("Xodimlar");


                HSSFRow row=xssfSheet.createRow(0);
                row.createCell(0).setCellValue("KUN");
                row.createCell(1).setCellValue("SOAT");
                row.createCell(2).setCellValue("ISM");

                CellStyle style=xssfWorkbook.createCellStyle();
//                style.setFillBackgroundColor(IndexedColors.RED1.getIndex());
                style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
                style.setFillPattern(FillPatternType.FINE_DOTS);


                for (int i = 0; i < users.size(); i++) {
                    row=xssfSheet.createRow(i+1);
                    row.createCell(0).setCellValue(users.get(i).getDate());

                    HSSFCell cell=row.createCell(0);
                    cell.setCellValue(users.get(i).getTime());
                    if (!(users.get(i).getTime().startsWith("08"))){
                        cell.setCellStyle(style);
                    }
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
