package com.juricamigac.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "izostanak")
public class Izostanak {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String datumIzostanka;
    private String razlogIzostanka;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatumIzostanka() {
        return datumIzostanka;
    }

    public void setDatumIzostanka(String datumIzostanka) {
        this.datumIzostanka = datumIzostanka;
    }

    public String getRazlogIzostanka() {
        return razlogIzostanka;
    }

    public void setRazlogIzostanka(String razlogIzostanka) {
        this.razlogIzostanka = razlogIzostanka;
    }
}
