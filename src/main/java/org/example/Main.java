package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {


    File fileEnova;   //creating a new file Enova instance
    FileInputStream fisEnova;   //obtaining bytes from the file Enova
    XSSFWorkbook wbEnova; //creating Workbook instance that refers to .xlsx file Enova
    XSSFSheet sheetEnova;     //creating a Sheet Enova object to retrieve object
    List<String> previousList = new ArrayList<>();
    List<String> nowList = new ArrayList<>();
    List<String> uniqueList;
    int lastRowNumberEnova;
    int columnNumber = 13;
    List<String> uniquePhoneList = new ArrayList<>();



    public Main() throws IOException {
    }

    public void initiateFileEnova(String path) throws IOException {
        fileEnova = new File(path);
        fisEnova = new FileInputStream(fileEnova);
        wbEnova = new XSSFWorkbook(fisEnova);
        sheetEnova = wbEnova.getSheetAt(0);
        lastRowNumberEnova = sheetEnova.getLastRowNum();
        XSSFCell cell = sheetEnova.getRow(1).getCell(1);
        Date dataDokumentu = cell.getDateCellValue();
        LocalDate localDate = dataDokumentu.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatedDate = localDate.format(formatter);
        System.out.println(formatedDate);
        String[] split = formatedDate.split("-");
        int i = Integer.valueOf(split[1]) - 1;
        System.out.println(i);

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
        System.out.println("lastRowNumber = " + lastRowNumberEnova);
        for (int i = 1; i <= lastRowNumberEnova; i++) {
            Row row = sheetEnova.getRow(i);
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
            for (int i = 1; i <= lastRowNumberEnova; i++) {
                Row row = sheetEnova.getRow(i);
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

