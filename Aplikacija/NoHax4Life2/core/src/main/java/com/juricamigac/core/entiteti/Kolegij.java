package com.juricamigac.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "kolegij")
public class Kolegij {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String naziv;
    private int brojIzostanaka;
    private String nazivIzvodenjaKolegija;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojIzostanaka() {
        return brojIzostanaka;
    }

    public void setBrojIzostanaka(int brojIzostanaka) {
        this.brojIzostanaka = brojIzostanaka;
    }

    public String getNazivIzvodenjaKolegija() {
        return nazivIzvodenjaKolegija;
    }

    public void setNazivIzvodenjaKolegija(String nazivIzvodenjaKolegija) {
        this.nazivIzvodenjaKolegija = nazivIzvodenjaKolegija;
    }
}
