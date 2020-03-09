package com.juricamigac.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.juricamigac.core.entiteti.Kolegij;

import java.util.List;

@Dao
public interface KolegijDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] unosKolegija(Kolegij... kolegiji);

    @Update
    public void azuriranjeKolegija(Kolegij... kolegiji);

    @Delete
    public void brisanjeKolegija(Kolegij... kolegiji);

    @Update
    public void azurirajKolegij(Kolegij... kolegiji);

    @Query("SELECT * FROM kolegij")
    public List<Kolegij> dohvatiSveKolegije();

    @Query("SELECT * FROM kolegij WHERE id = :id")
    public Kolegij dohvatiKolegij(int id);

    @Query("SELECT * FROM kolegij")
    public LiveData<List<Kolegij>> dohvatiSveKolegijeLIVE();

    @Query("SELECT * FROM kolegij WHERE id = :id")
    public LiveData<Kolegij> dohvatiKolegijLIVE(int id);

    @Query("DELETE FROM kolegij")
    public void izbrisiSveKolegije();
}
