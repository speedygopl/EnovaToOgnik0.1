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
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    File fileEnova;   //creating a new file Enova instance
    FileInputStream fisEnova;   //obtaining bytes from the file Enova
    XSSFWorkbook wbEnova; //creating Workbook instance that refers to .xlsx file Enova
    XSSFSheet sheetEnova;     //creating a Sheet Enova object to retrieve object
    OgnikClass ognikClass = new OgnikClass();
    int fiscalMonth;
    int fiscalYear = 2024;
    int lastDayOfFiscalMonth;

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
        setFiscalMonth();
        createNumerDokumentu();
        createDataDokumentu();
        createOpisDokumentu();
        createDataKsiegowaniaDataZdarzenia();
        System.out.println(ognikClass.toString());

    }

    public void createNumerDokumentu() {
        final XSSFCell cell1 = sheetEnova.getRow(1).getCell(2);
        String input = cell1.toString();
        String regex = "\\((\\d+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String extractedValue = matcher.group(1);
            System.out.println("Extracted value: " + extractedValue);
        } else {
            System.out.println("No match found.");
        }
        ognikClass.setNumerDokumentu("1/" + (fiscalMonth <= 9 ? "0" + fiscalMonth : fiscalMonth) + "/" + matcher.group(1) + "/"+fiscalYear); //set document number in OgnikClass

    }

    public void setFiscalMonth(){ //should run before other methods
        XSSFCell cell = sheetEnova.getRow(1).getCell(1);
        Date date = cell.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(formatter);
        String[] split = formattedDate.split("-");
        fiscalMonth = Integer.valueOf(split[1]) - 1;
    }

    public void createDataDokumentu() {
        XSSFCell cell = sheetEnova.getRow(1).getCell(1);
        Date date = cell.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ognikClass.setDataDokumentu(localDate); // set document date in OgnikClass
    }

    public void createOpisDokumentu() {
        final XSSFCell cell1 = sheetEnova.getRow(1).getCell(2);
        String[] opisDokumentuArray = cell1.toString().split("\\((\\d+)\\)");
        String opisDokumentuPart = opisDokumentuArray[0];
        ognikClass.setOpisDokumentu(ognikClass.getNumerDokumentu() + " " + opisDokumentuPart);
    }

    public void createDataKsiegowaniaDataZdarzenia(){
        YearMonth yearMonth = YearMonth.of(fiscalYear, fiscalMonth);
        lastDayOfFiscalMonth = yearMonth.lengthOfMonth();
        LocalDate specificLocalDate = LocalDate.of(fiscalYear, fiscalMonth, lastDayOfFiscalMonth);
        ognikClass.setDataKsiegowania(specificLocalDate);
        ognikClass.setDataZdarzenia(specificLocalDate);
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

