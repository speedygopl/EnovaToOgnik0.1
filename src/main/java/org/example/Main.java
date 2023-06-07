package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\users\\ania\\downloads\\workbook.xlsx");   //creating a new file instance
        FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
        XSSFWorkbook wb = new XSSFWorkbook(fis); //creating Workbook instance that refers to .xlsx file
        XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
        int lastRowNumber = sheet.getLastRowNum();
        int columnNumber = 14;
        List<Cell> clientIdList = new ArrayList<>();

        for (int i = 1; i <= lastRowNumber; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnNumber);
            clientIdList.add(cell);
            System.out.println(cell.toString());
        }

    }
}