package com.juricamigac.core.entiteti;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "izostanciKolegija")
public class IzostanciKolegija {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = Kolegij.class,parentColumns = "id",childColumns = "idKolegij")
    private int idKolegij;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdKolegij() {
        return idKolegij;
    }

    public void setIdKolegij(int idKolegij) {
        this.idKolegij = idKolegij;
    }

    public int getIdIzostanka() {
        return idIzostanka;
    }

    public void setIdIzostanka(int idIzostanka) {
        this.idIzostanka = idIzostanka;
    }

    @ForeignKey(entity = Izostanak.class,parentColumns = "id",childColumns = "idIzostanka")
    private int idIzostanka;
}
