package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {


    File file;   //creating a new file instance
    FileInputStream fis;   //obtaining bytes from the file
    XSSFWorkbook wb; //creating Workbook instance that refers to .xlsx file
    XSSFSheet sheet;     //creating a Sheet object to retrieve object
    List<String> previousList = new ArrayList<>();
    List<String> nowList = new ArrayList<>();
    List<String> uniqueList;
    int lastRowNumber;
    int columnNumber = 13;
    List<String> uniquePhoneList = new ArrayList<>();



    public Main() throws IOException {
    }

    public void initiateFile(String path) throws IOException {
        file = new File(path);
        fis = new FileInputStream(file);
        wb = new XSSFWorkbook(fis);
        sheet = wb.getSheetAt(0);
        lastRowNumber = sheet.getLastRowNum();
    }

    public void runApp() throws IOException {
        makeNowList();
        makePreviousList();
        makeUniqueList();
        for (String str : uniqueList) {
            System.out.println(str);
        }
        makeUniquePhoneList();
        for (String str : uniquePhoneList) {
            System.out.println(str);
        }

    }

    public void makePreviousList() throws IOException {
        previousList = Files.readAllLines(new File("previous.txt").toPath(), Charset.defaultCharset());
    }

    public void makeNowList() {
        System.out.println("lastRowNumber = " + lastRowNumber);
        for (int i = 1; i <= lastRowNumber; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(columnNumber);
            nowList.add(cell.toString());
        }
    }

    public void makeUniqueList() {
        uniqueList = new ArrayList<String>(nowList);
        uniqueList.removeAll(previousList);
    }

    public void makeUniquePhoneList() {
        for (String str : uniqueList) {
            for (int i = 1; i <= lastRowNumber; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(columnNumber);
                if (cell.toString().equals(str) && row.getCell(5) != null) {
                    Cell phoneCell = row.getCell(5);
                    phoneCell.setCellType(CellType.STRING);
                    uniquePhoneList.add(phoneCell.toString());
                }
            }
        }
    }

    public void writeListIntoFile(List<String> list, String pathName) throws IOException {
        if (!list.isEmpty()) {
            File file = new File(pathName);
            FileWriter fileWriter = new FileWriter(file);
            for (String str : list) {
                fileWriter.write(str + System.lineSeparator());
            }
            fileWriter.close();
        } else {
            System.out.println("Lista jest pusta!");
        }
    }

}

