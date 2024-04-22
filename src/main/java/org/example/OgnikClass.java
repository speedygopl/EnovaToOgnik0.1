package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class OgnikClass {

    String numerDokumentu;
    String opisDokumentu;
    LocalDate dataKsiegowania;
    LocalDate dataDokumentu;
    LocalDate dataZdarzenia;
    String przedsiebiorca = "500-01-02-01-01-04";
    String pracownik;
    String zus = "222-01";
    String pit = "221-01";
    Double kwota;
    String opisDekretuWynagrodzenie = "Wynagrodzenie - obsługa";
    String opisDekretuZusPracodawcy = "Składki ZUS pracodawcy";
    String opisDekretuZuspracownika = "Składki ZUS pracownika";
    String opisDekretuPodatekPit = "Podatek PIT-4";

    public String getNumerDokumentu() {
        return numerDokumentu;
    }

    public void setNumerDokumentu(String numerDokumentu) {
        this.numerDokumentu = numerDokumentu;
    }

    public String getOpisDokumentu() {
        return opisDokumentu;
    }

    public void setOpisDokumentu(String opisDokumentu) {
        this.opisDokumentu = opisDokumentu;
    }

    public LocalDate getDataKsiegowania() {
        return dataKsiegowania;
    }

    public void setDataKsiegowania(LocalDate dataKsiegowania) {
        this.dataKsiegowania = dataKsiegowania;
    }

    public LocalDate getDataDokumentu() {
        return dataDokumentu;
    }

    public void setDataDokumentu(LocalDate dataDokumentu) {
        this.dataDokumentu = dataDokumentu;
    }

    public LocalDate getDataZdarzenia() {
        return dataZdarzenia;
    }

    public void setDataZdarzenia(LocalDate dataZdarzenia) {
        this.dataZdarzenia = dataZdarzenia;
    }

    public String getPracownik() {
        return pracownik;
    }

    public void setPracownik(String pracownik) {
        this.pracownik = pracownik;
    }

    public Double getKwota() {
        return kwota;
    }

    public void setKwota(Double kwota) {
        this.kwota = kwota;
    }

    @Override
    public String toString() {
        return "OgnikClass{" +
                "numerDokumentu='" + numerDokumentu + '\'' +
                ", opisDokumentu='" + opisDokumentu + '\'' +
                ", dataKsiegowania=" + dataKsiegowania +
                ", dataDokumentu=" + dataDokumentu +
                ", dataZdarzenia=" + dataZdarzenia +
                ", przedsiebiorca='" + przedsiebiorca + '\'' +
                ", pracownik='" + pracownik + '\'' +
                ", zus='" + zus + '\'' +
                ", pit='" + pit + '\'' +
                ", kwota=" + kwota +
                ", opisDekretuWynagrodzenie='" + opisDekretuWynagrodzenie + '\'' +
                ", opisDekretuZusPracodawcy='" + opisDekretuZusPracodawcy + '\'' +
                ", opisDekretuZuspracownika='" + opisDekretuZuspracownika + '\'' +
                ", opisDekretuPodatekPit='" + opisDekretuPodatekPit + '\'' +
                '}';
    }
}
