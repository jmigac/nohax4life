package com.juricamigac.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.juricamigac.core.entiteti.Izostanak;
import com.juricamigac.core.entiteti.IzostanciKolegija;

import java.util.List;

@Dao
public interface IzostanakDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] unosIzostanka(Izostanak... izostanci);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] unosIzostankaNaKolegiju(IzostanciKolegija... izostanciKolegija);

    @Update
    public void azurirajIzostanak(Izostanak... izostanci);

    @Update
    public void azurirajIzostanakKolegija(IzostanciKolegija... izostanciKolegija);

    @Query("SELECT * FROM izostanak")
    public List<Izostanak> dohvatiSveIzostanke();

    @Query("SELECT * FROM izostanak iz JOIN izostanciKolegija ik ON iz.id=ik.idIzostanka WHERE ik.idKolegij = :idKolegija")
    public List<Izostanak> dohvatiSveIzostankeKolegija(int idKolegija);

    @Query("SELECT * FROM izostanciKolegija WHERE id = :idIzostankaKolegija")
    public IzostanciKolegija dohvatiIzostanakKolegija(int idIzostankaKolegija);

    @Query("DELETE FROM izostanak")
    public void izbrisiSveIzostanke();

    @Query("DELETE FROM izostanciKolegija")
    public void izbrisiSveIzostankeKolegija();

    @Query("DELETE FROM izostanak WHERE id = :id")
    public void izbrisiIzostanak(int id);

    @Query("DELETE FROM izostanciKolegija WHERE id = :id")
    public void izbrisiIzostanakKolegija(int id);

    @Query("DELETE FROM izostanciKolegija WHERE idKolegij = :idKolegij")
    public void izbrisiSveIzostankeOdredenogKolegija(int idKolegij);

    @Query("SELECT COUNT(*) FROM izostanciKolegija WHERE idKolegij = :idKolegija")
    public int dohvatiBrojIzostanakaOdredenogKolegija(int idKolegija);

    @Query("SELECT * FROM izostanak iz JOIN izostanciKolegija ik ON iz.id=ik.idIzostanka WHERE ik.idKolegij = :idKolegija")
    public LiveData<List<Izostanak>> dohvatiSveIzostankeKolegijaLIVE(int idKolegija);

    @Query("SELECT COUNT(*) FROM izostanciKolegija WHERE idKolegij = :id")
    public LiveData<Integer> dohvatiBrojIzostanakaOdredenogKolegijaLIVE(int id);
}
