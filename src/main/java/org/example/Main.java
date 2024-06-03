package org.example;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.Collator;
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
    List<Map<String, String>> listOfMaps = new ArrayList<>();
    Map<String, String> mapWynagrodzenieOpiekuna = new LinkedHashMap<>();
    Map<String, String> mapSkladkiZusPracodawcy = new LinkedHashMap<>();
    Map<String, String> mapSkladkiZusPracownika = new LinkedHashMap<>();
    Map<String, String> mapPodatekPit4 = new LinkedHashMap<>();
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
    }

    public String findCodeIntoPlanKontByName(String name) {
        String code = "";
        for (int i = 1; i <= lastRowNumberPlan; i++) {
            XSSFCell cell = sheetPlan.getRow(i).getCell(1);
            if (cell.toString().toLowerCase().contains(name.toLowerCase())) {
//                System.out.println(cell.toString());
//                System.out.println(sheetPlan.getRow(i).getCell(0).toString());
                return sheetPlan.getRow(i).getCell(0).toString();
            }
        }
        System.out.println("koniec");
        return "";
    }


    public String createNumerDokumentu(int i) {
        final XSSFCell cell1 = sheetEnova.getRow(i).getCell(2);
        String input = cell1.toString();
        String regex = "\\((\\d+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        String extractedValue = "";
        if (matcher.find()) {
            extractedValue = matcher.group(1);
//            System.out.println("Extracted value: " + extractedValue);
        } else {
//            System.out.println("No match found.");
        }
        return "1/" + (fiscalMonth <= 9 ? "0" + fiscalMonth : fiscalMonth) + "/" + extractedValue + "/" + fiscalYear;

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

    public String createDataDokumentu(int i) {
        XSSFCell cell = sheetEnova.getRow(i).getCell(1);
        Date date = cell.getDateCellValue();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.toString();
    }

    public String createOpisDokumentu(int i) {
        final XSSFCell cell1 = sheetEnova.getRow(i).getCell(2);
        String[] opisDokumentuArray = cell1.toString().split("\\((\\d+)\\)");
        String opisDokumentuPart = opisDokumentuArray[0];
        return mapWynagrodzenieOpiekuna.get("NumerDokumentu") + " " + opisDokumentuPart;
    }

    public String createDataKsiegowaniaDataZdarzenia() {
        YearMonth yearMonth = YearMonth.of(fiscalYear, fiscalMonth);
        lastDayOfFiscalMonth = yearMonth.lengthOfMonth();
        LocalDate specificLocalDate = LocalDate.of(fiscalYear, fiscalMonth, lastDayOfFiscalMonth);
        return specificLocalDate.toString();
    }

    public void create4MapsWynagrodzenieZusPit(int i) {
        XSSFCell cellKwotaWynagrodzeniaBrutto = sheetEnova.getRow(i).getCell(3);
        XSSFCell cellName = sheetEnova.getRow(i).getCell(2);
        XSSFCell cellKwotaZusPracodawcy = sheetEnova.getRow(i).getCell(4);
        XSSFCell cellKwotaFP = sheetEnova.getRow(i).getCell(5);
        XSSFCell cellKwotaFG = sheetEnova.getRow(i).getCell(6);
        XSSFCell cellKwotaZusPracownika = sheetEnova.getRow(i).getCell(7);
        XSSFCell cellKwotaZusPracownika26 = sheetEnova.getRow(i).getCell(8);
        XSSFCell cellKwotaZusPracownikaZdrowotne = sheetEnova.getRow(i).getCell(9);
        XSSFCell cellKwotaZaliczkaFiskalna = sheetEnova.getRow(i).getCell(10);
        Double value = Double.valueOf(cellKwotaZusPracodawcy.toString()) + Double.valueOf(cellKwotaFP.toString()) + Double.valueOf(cellKwotaFG.toString());
        Double skladkiZusPracodawcy = Math.round(value * 100.0) / 100.0;
        Double value1 = Double.valueOf(cellKwotaZusPracownika.toString()) + Double.valueOf(cellKwotaZusPracownika26.toString()) + Double.valueOf(cellKwotaZusPracownikaZdrowotne.toString());
        Double skladkiZusPracownika = Math.round(value1 * 100.0) / 100.0;

        mapWynagrodzenieOpiekuna.put("NumerDokumentu", createNumerDokumentu(i));
        mapWynagrodzenieOpiekuna.put("OpisDokumentu", createOpisDokumentu(i));
        mapWynagrodzenieOpiekuna.put("DataKsiegowania", createDataKsiegowaniaDataZdarzenia());
        mapWynagrodzenieOpiekuna.put("DataDokumentu", createDataDokumentu(i));
        mapWynagrodzenieOpiekuna.put("DataZdarzenia", createDataKsiegowaniaDataZdarzenia());
        mapWynagrodzenieOpiekuna.put("KontoWn", "500-01-02-02-01-01");
        mapWynagrodzenieOpiekuna.put("KontoMa", findCodeIntoPlanKontByName(nameExtractor(cellName.toString())));
        mapWynagrodzenieOpiekuna.put("Kwota", cellKwotaWynagrodzeniaBrutto.toString());
        mapWynagrodzenieOpiekuna.put("OpisDekretu", "Wynagrodzenie - opiekuna");
        listOfMaps.add(new LinkedHashMap<>(mapWynagrodzenieOpiekuna));
        if (skladkiZusPracodawcy != 0) {
            mapSkladkiZusPracodawcy.put("NumerDokumentu", createNumerDokumentu(i));
            mapSkladkiZusPracodawcy.put("OpisDokumentu", createOpisDokumentu(i));
            mapSkladkiZusPracodawcy.put("DataKsiegowania", createDataKsiegowaniaDataZdarzenia());
            mapSkladkiZusPracodawcy.put("DataDokumentu", createDataDokumentu(i));
            mapSkladkiZusPracodawcy.put("DataZdarzenia", createDataKsiegowaniaDataZdarzenia());
            mapSkladkiZusPracodawcy.put("KontoWn", "500-01-02-02-01-01");
            mapSkladkiZusPracodawcy.put("KontoMa", "222-01");
            mapSkladkiZusPracodawcy.put("Kwota", String.valueOf(skladkiZusPracodawcy));
            mapSkladkiZusPracodawcy.put("OpisDekretu", "Składki ZUS Pracodawcy");
            listOfMaps.add(new LinkedHashMap<>(mapSkladkiZusPracodawcy));
        }
        if (skladkiZusPracownika != 0) {
            mapSkladkiZusPracownika.put("NumerDokumentu", createNumerDokumentu(i));
            mapSkladkiZusPracownika.put("OpisDokumentu", createOpisDokumentu(i));
            mapSkladkiZusPracownika.put("DataKsiegowania", createDataKsiegowaniaDataZdarzenia());
            mapSkladkiZusPracownika.put("DataDokumentu", createDataDokumentu(i));
            mapSkladkiZusPracownika.put("DataZdarzenia", createDataKsiegowaniaDataZdarzenia());
            mapSkladkiZusPracownika.put("KontoWn", findCodeIntoPlanKontByName(nameExtractor(cellName.toString())));
            mapSkladkiZusPracownika.put("KontoMa", "222-01");
            mapSkladkiZusPracownika.put("Kwota", String.valueOf(skladkiZusPracownika));
            mapSkladkiZusPracownika.put("OpisDekretu", "Składki ZUS Pracownika");
            listOfMaps.add(new LinkedHashMap<>(mapSkladkiZusPracownika));
        }
        if (Double.valueOf(cellKwotaZaliczkaFiskalna.toString()) != 0) {
            mapPodatekPit4.put("NumerDokumentu", createNumerDokumentu(i));
            mapPodatekPit4.put("OpisDokumentu", createOpisDokumentu(i));
            mapPodatekPit4.put("DataKsiegowania", createDataKsiegowaniaDataZdarzenia());
            mapPodatekPit4.put("DataDokumentu", createDataDokumentu(i));
            mapPodatekPit4.put("DataZdarzenia", createDataKsiegowaniaDataZdarzenia());
            mapPodatekPit4.put("KontoWn", findCodeIntoPlanKontByName(nameExtractor(cellName.toString())));
            mapPodatekPit4.put("KontoMa", "221-01");
            mapPodatekPit4.put("Kwota", cellKwotaZaliczkaFiskalna.toString());
            mapPodatekPit4.put("OpisDekretu", "Podatek PIT-4");
            listOfMaps.add(new LinkedHashMap<>(mapPodatekPit4));
        }

//        mapWynagrodzenieOpiekuna.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
//        mapSkladkiZusPracodawcy.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
//        mapSkladkiZusPracownika.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
//        mapPodatekPit4.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));

    }


    public void makeListOfMapsAndCreateResultsFile() throws IOException, InvalidFormatException {
        for (int k = 1; k <= lastRowNumberEnova; k++) {
            create4MapsWynagrodzenieZusPit(k);
        }
//        for(Map<String, String> mapa: listOfMaps){
//            for (Map.Entry<String, String> entry : mapa.entrySet()) {
//                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//            }
//        }
        Collections.sort(listOfMaps, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                String name1 = extractLastName(o1.get("OpisDokumentu"));
                String name2 = extractLastName(o2.get("OpisDokumentu"));

                Collator collator = Collator.getInstance(new Locale("pl", "PL"));
                return collator.compare(name1, name2);
            }

            private String extractLastName(String opisDokumentu) {
                String[] parts = opisDokumentu.split(" ");
                return parts[1];  // Assuming the format is always [number last_name first_name]
            }
        });


        System.out.println(listOfMaps.size());
        XSSFWorkbook wbOgnik = new XSSFWorkbook();
        XSSFSheet sheetOgnik = wbOgnik.createSheet();

        //create header row
        Row headerRow = sheetOgnik.createRow(0);
        List<String> headers = new ArrayList<>(listOfMaps.get(0).keySet());
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
        }

        //fill data
        int rowNum = 1;
        for (Map<String, String> rowData : listOfMaps) {
            Row row = sheetOgnik.createRow(rowNum++);
            int cellNum = 0;
            for (String key : headers) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(rowData.get(key));
            }
        }

        //auto size columns
        for (int i = 0; i < headers.size(); i++) {
            sheetOgnik.autoSizeColumn(i);
        }

        //write the output to the file
        try (FileOutputStream fileOut = new FileOutputStream("Ogink.xlsx")) {
            wbOgnik.write(fileOut);
            System.out.println("Excel file has been written successfully!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //close the workbook
        try {
            wbOgnik.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String nameExtractor(String cellName) {
        Pattern pattern = Pattern.compile("([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9_]+(-[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9_]+)?) ([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ0-9_]+) (\\(\\d+\\))");
        Matcher matcher = pattern.matcher(cellName);
        if (matcher.find()) {
            String lastName = matcher.group(1);
            String firstName = matcher.group(3);
            return lastName + " " + firstName;
        } else {
            System.out.println(cellName + " : No match found");
        }
        return "----";
    }


}




