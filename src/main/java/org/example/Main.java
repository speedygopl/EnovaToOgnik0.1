package org.example;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    File fileEnova;   //creating a new file Enova instance
    FileInputStream fisEnova;   //obtaining bytes from the file Enova
    XSSFWorkbook wbEnova; //creating Workbook instance that refers to .xlsx file Enova
    XSSFSheet sheetEnova;     //creating a Sheet Enova object to retrieve object
    File filePlan;
    FileInputStream fisPlan;
    XSSFWorkbook wbPlan;
    XSSFSheet sheetPlan;
    OgnikClass ognikClass = new OgnikClass();
    int fiscalMonth;
    int fiscalYear = 2024;
    int lastDayOfFiscalMonth;
    List<List<Map<String, String>>> listOfListsOfMaps = new ArrayList<>();
    List<Map<String, String>> listOfMaps = new ArrayList<>();
    Map<String, String> mapOfStrings = new HashMap<>();
    int lastRowNumberEnova;
    int lastRowNumberPlan;


    public Main() throws IOException {
    }

    public void initiateFileEnova(String path) throws IOException {
        fileEnova = new File(path);
        fisEnova = new FileInputStream(fileEnova);
        wbEnova = new XSSFWorkbook(fisEnova);
        sheetEnova = wbEnova.getSheetAt(0);
        lastRowNumberEnova = sheetEnova.getLastRowNum();

    }

    public void initiateFilePlan(String path) throws IOException {
        filePlan = new File(path);
        fisPlan = new FileInputStream(filePlan);
        wbPlan = new XSSFWorkbook(fisPlan);
        sheetPlan = wbPlan.getSheetAt(0);
        lastRowNumberPlan = sheetPlan.getLastRowNum();
        findCodeIntoPlanKontByName();

    }

    public String findCodeIntoPlanKontByName(){
        String code ="";
        for(int i = 1; i <= lastRowNumberPlan; i++) {
            XSSFCell cell = sheetPlan.getRow(i).getCell(2);
            System.out.println(cell.toString());
            if (cell.toString().contains("Marczak Izabela")){
                System.out.println(sheetPlan.getRow(i).getCell(0).toString());
                return sheetPlan.getRow(i).getCell(0).toString();
            } else {
                System.out.println("brak");
            }
        }
        System.out.println("koniec");
        return "";
    }


    public String createNumerDokumentu() {
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
        return "1/" + (fiscalMonth <= 9 ? "0" + fiscalMonth : fiscalMonth) + "/" + matcher.group(1) + "/" + fiscalYear;

    }

    public void setFiscalMonth() { //should run before other methods
        XSSFCell cell = sheetEnova.getRow(1).getCell(1);
        Date date = cell.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDate.format(formatter);
        String[] split = formattedDate.split("-");
        fiscalMonth = Integer.valueOf(split[1]) - 1;
    }

    public String createDataDokumentu() {
        XSSFCell cell = sheetEnova.getRow(1).getCell(1);
        Date date = cell.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.toString();
    }

    public String createOpisDokumentu() {
        final XSSFCell cell1 = sheetEnova.getRow(1).getCell(2);
        String[] opisDokumentuArray = cell1.toString().split("\\((\\d+)\\)");
        String opisDokumentuPart = opisDokumentuArray[0];
        return mapOfStrings.get("NumerDokumentu") + " " + opisDokumentuPart;
    }

    public String createDataKsiegowaniaDataZdarzenia() {
        YearMonth yearMonth = YearMonth.of(fiscalYear, fiscalMonth);
        lastDayOfFiscalMonth = yearMonth.lengthOfMonth();
        LocalDate specificLocalDate = LocalDate.of(fiscalYear, fiscalMonth, lastDayOfFiscalMonth);
        return specificLocalDate.toString();
    }

    public void wynagrodzenieOpiekunaToMap() {
        XSSFCell cell = sheetEnova.getRow(1).getCell(3);
        mapOfStrings.put("NumerDokumentu", createNumerDokumentu());
        mapOfStrings.put("OpisDokumentu", createOpisDokumentu());
        mapOfStrings.put("DataKsiegowania", createDataKsiegowaniaDataZdarzenia());
        mapOfStrings.put("DataDokumentu", createDataDokumentu());
        mapOfStrings.put("DataZdarzenia", createDataKsiegowaniaDataZdarzenia());
        mapOfStrings.put("KontoWn", "500-01-02-01-01-05");
        mapOfStrings.put("KontoMa", "TODO"); //todo
        mapOfStrings.put("Kwota", cell.toString());
        mapOfStrings.put("OpisDekretu", "Wynagrodzenie - opiekuna");
    }


}




